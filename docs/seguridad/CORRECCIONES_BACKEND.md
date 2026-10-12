# Correcciones de seguridad del backend (Ciberseguridad)

Backend corregido: `ISPC-TSDWAD/ModPWeb--Dev6` (carpeta `Backend/`), rama `feature/jonathan-guillen`.
Servidor de pruebas: `https://dev6.pythonanywhere.com`.

> Todos los ejemplos usan usuarios y claves **de prueba creados solo para la demostración**. No hay claves ni tokens reales.

Qué se corrigió:

| # | Problema | Corrección | Archivo(s) |
|---|---|---|---|
| 18 | Un usuario común podía subirse a admin con un `PATCH` | `is_staff`, `is_active` y `rol` pasan a solo lectura | `users/serializers.py` |
| 19 | El JWT no llevaba `is_staff` ni `rol` (solo iban en el cuerpo de la respuesta) | Claims reales dentro del token firmado | `users/serializers.py` |
| — | No había registros de seguridad | Log de logins fallidos, accesos denegados y cambios de rol | `users/security_log.py`, `core/exceptions.py`, `users/views.py`, `core/settings.py` |
| — | Con los campos en solo lectura, el admin no podía asignar roles | Endpoint admin-only `POST /api/users/{id}/set-role/` | `users/views.py` |

---

## #18 — Escalada de privilegios (`is_staff`, `rol`, `is_active`)

### Qué pasaba (explicación para la defensa)

`UsuarioSerializer` listaba `is_staff`, `is_active` y `rol` como campos normales, o sea **escribibles**. El permiso `IsAdminOrOwner` deja que cada usuario edite *su propio* perfil. Entonces un usuario común podía editar su propio perfil **y de paso cambiarse el rol**. Es una vulnerabilidad de *mass assignment* (asignación masiva): el servidor acepta campos que el cliente nunca debió poder tocar.

### Prueba ANTES de la corrección

Usuario de prueba `asesor_poc` (rol `asesor`), autenticado con su propio token:

```
PATCH /api/users/1/
Authorization: Bearer <access_de_asesor_poc>
{"is_staff": true, "rol": "admin", "is_active": false, "first_name": "Mario"}

-> 200
{"id": 1, "username": "asesor_poc", "email": "", "first_name": "Mario", "last_name": "",
 "is_staff": true, "is_active": false, "rol": "admin"}

Base de datos: is_staff=True, rol=admin, is_active=False   <-- ¡se ascendió solo!
```

### Corrección

```python
class Meta:
    model = Usuario
    fields = ['id', 'username', 'email', 'first_name', 'last_name', 'is_staff', 'is_active', 'password', 'rol']
    read_only_fields = ['is_staff', 'is_active', 'rol']
```

`read_only_fields` hace que DRF **lea** esos campos al responder pero **ignore** lo que llegue en el pedido.

### Prueba DESPUÉS de la corrección

Mismo pedido, mismo usuario:

```
PATCH /api/users/1/
{"is_staff": true, "rol": "admin", "is_active": false, "first_name": "Mario"}

-> 200
{"id": 1, "username": "asesor_poc", "email": "", "first_name": "Mario", "last_name": "",
 "is_staff": false, "is_active": true, "rol": "asesor"}

Base de datos: is_staff=False, rol=asesor, is_active=True   <-- no cambió
```

El campo de perfil (`first_name`) **sí** se actualizó; los campos protegidos **no**.

### Cómo probarlo vos mismo (esperado entre paréntesis)

```bash
BASE="https://dev6.pythonanywhere.com"
# 1) login con una cuenta NO admin (clave: pedírsela al responsable del deploy)
curl -s -X POST "$BASE/api/token/" -H "Content-Type: application/json" \
     -d '{"username":"asesor_demo","password":"<clave>"}'          # (200 + access)

# 2) intentar ascenderse (reemplazar <id> por el id del propio usuario: GET /api/users/me/)
curl -s -X PATCH "$BASE/api/users/<id>/" -H "Authorization: Bearer <access>" \
     -H "Content-Type: application/json" \
     -d '{"is_staff": true, "rol": "admin"}'                       # (200, pero is_staff=false y rol="asesor")
```

Además hay pruebas automáticas: `python manage.py test users` (clase `EscaladaDePrivilegiosTests`, incluye PATCH, PUT, editar a otro usuario → 403 y alta de usuario sin poder fijar rol).

### Consecuencia y cómo se resolvió: `set-role`

Con los campos en solo lectura, ni el admin podía asignar roles por la API. Se agregó **un único camino** auditable:

```
POST /api/users/{id}/set-role/        (solo is_staff)
{"rol": "maquetador"}                 -> 200 con el usuario actualizado
{"rol": "docente"}                    -> 400 {"rol": ["\"docente\" no es una elección válida."]}
(un asesor intentándolo)              -> 403
```

Reglas: `rol=admin` marca también `is_staff=true` (el permiso `IsAdminOrOwner` se basa en `is_staff`); no se puede cambiar el rol de un superusuario; un admin no puede quitarse a sí mismo el rol de admin (evita quedarse sin administradores). Los roles válidos son solo `admin`, `asesor` y `maquetador`.

---

## #19 — Claims reales en el JWT

### Qué pasaba

`CustomTokenObtainPairSerializer` agregaba `is_staff` y `rol` solo al **cuerpo de la respuesta** del login. El token en sí traía únicamente `user_id`. Un cliente que decodifica el token para saber el rol no encontraba nada.

### Corrección

Se sobrescribe `get_token`, que es el método de SimpleJWT que arma el contenido del token:

```python
@classmethod
def get_token(cls, user):
    token = super().get_token(user)
    token['is_staff'] = user.is_staff
    token['rol'] = rol_efectivo(user)   # superusuario => 'admin'
    return token
```

El cuerpo de la respuesta sigue incluyendo `is_staff` y `rol` para no romper a quien ya los lee de ahí.

### Prueba

Payload del `access` ANTES: `{"token_type": "access", "user_id": 1}`
Payload del `access` DESPUÉS: `{"token_type": "access", "user_id": 1, "is_staff": false, "rol": "asesor"}`

### Explicación para la defensa

- Los claims van **firmados** con la `SECRET_KEY`. Si alguien edita el payload (por ejemplo cambia `rol` a `admin`), la firma deja de coincidir y el servidor responde `401`. Está probado en `test_token_con_claims_alterados_es_rechazado`.
- Aun así, el rol del token es **informativo para el cliente** (por ejemplo para mostrar u ocultar botones). Los permisos reales los decide siempre el servidor leyendo la base de datos, no el contenido del token.
- Un token no es secreto: cualquiera puede *leer* su payload (es Base64). Por eso no se guardan datos sensibles ahí.

---

## Logs de seguridad

Archivo: `Backend/logs/security.log` (carpeta ignorada por git; configurable con `LOG_DIR`). Rotación automática a 1 MB con 5 copias. Cada línea incluye fecha y hora (con zona horaria), usuario, IP, acción, recurso y resultado.

Muestra real generada con usuarios de prueba:

```
2026-10-12 12:52:31-0300 | WARNING | user=ase_demo | ip=127.0.0.1 | action=LOGIN_FAILED | resource=/api/token/ | result=fallido (401)
2026-10-12 12:52:32-0300 | INFO | user=ase_demo | ip=127.0.0.1 | action=LOGIN | resource=/api/token/ | result=exitoso
2026-10-12 12:52:32-0300 | WARNING | user=ase_demo | ip=127.0.0.1 | action=ACCESS_DENIED | resource=GET /api/users/ | result=denegado: permisos insuficientes
2026-10-12 12:52:33-0300 | WARNING | user=adm_demo | ip=127.0.0.1 | action=ROLE_CHANGE | resource=usuario:ase_demo | result=exitoso: asesor -> maquetador
2026-10-12 12:52:33-0300 | WARNING | user=anonimo | ip=127.0.0.1 | action=ACCESS_DENIED | resource=GET /api/pedagogia/recursos/ | result=denegado: no autenticado o token inválido
```

| Evento | Dónde se registra |
|---|---|
| Login fallido / exitoso | `CustomTokenObtainPairView.post` |
| Acceso denegado (401 y 403) | `core/exceptions.py` (manejador de excepciones de DRF, configurado en `REST_FRAMEWORK`) |
| Cambio de rol | acción `set_role` |
| Cambio de contraseña (extra) | acción `change_password` |

Decisiones de diseño (para la defensa):

- **Nunca** se registran contraseñas ni tokens: solo nombre de usuario, IP, método + ruta (sin query string) y resultado. Hay tests que lo verifican (`test_login_fallido_se_registra_sin_la_contrasena`, `test_login_exitoso_se_registra_sin_tokens_ni_contrasena`).
- Se limpian saltos de línea y barras `|` de los valores ingresados por el usuario para evitar *log injection* (que alguien fabrique líneas falsas en el log). Test: `test_un_nombre_de_usuario_con_saltos_de_linea_no_inyecta_lineas_falsas`.
- La IP sale de `X-Forwarded-For` porque PythonAnywhere atiende detrás de un proxy.
- Los cambios de rol y los accesos denegados van como `WARNING` para filtrarlos rápido: `grep WARNING security.log`.

Para mirarlo en el servidor: `tail -n 30 ~/ModPWeb--Dev6/Backend/logs/security.log`.

---

## Cómo correr todas las pruebas

```bash
cd Backend
export SECRET_KEY=cualquier-valor-de-prueba DEBUG=True DB_ENGINE=sqlite
python manage.py test users pedagogia
# Resultado: Ran 26 tests ... OK
```

## Otras correcciones de seguridad del mismo deploy (commit anterior)

- `DEBUG=False` por defecto y `SECRET_KEY` leída de una variable de entorno (el servidor no arranca si falta).
- CORS cerrado: `CORS_ALLOW_ALL_ORIGINS=False` y lista explícita de orígenes (probado: un origen ajeno no recibe `Access-Control-Allow-Origin`).
- HTTPS forzado en el servidor (`http://` redirige a `https://`) y cookies marcadas como seguras.
- Superusuario nuevo con clave propia; la cuenta de prueba `admin` no existe en el servidor; credenciales eliminadas del README; el seed ya no tiene clave por defecto.
