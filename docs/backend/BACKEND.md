# Backend de EduTools

La app Android consume una API REST hecha en Django. El código del backend está en otro repositorio, pero **todo lo que hay que saber para usarlo, desplegarlo y probarlo está documentado acá**.

| Dato | Valor |
|---|---|
| Código del backend | [ISPC-TSDWAD/ModPWeb--Dev6](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6) (carpeta `Backend/`) |
| Servidor de pruebas | `https://dev6.pythonanywhere.com` |
| Cambios de Sprint 2 | [PR #64 del backend](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6/pull/64) (rama `feature/jonathan-guillen`) |
| Lista de endpoints | [docs/api/ENDPOINTS.md](../api/ENDPOINTS.md) |
| Correcciones de seguridad | [docs/seguridad/CORRECCIONES_BACKEND.md](../seguridad/CORRECCIONES_BACKEND.md) |

## 1. Tecnologías

| Elemento | Versión |
|---|---|
| Python | 3.13 (en PythonAnywhere) |
| Django | 6.0.4 |
| Django REST Framework | 3.17.1 |
| SimpleJWT (autenticación) | 5.3.1 |
| django-cors-headers | 4.9.0 |
| python-dotenv | 1.2.2 |
| Base de datos | SQLite en el servidor de pruebas (MySQL con `DB_ENGINE=mysql`) |

Apps del proyecto: `users` (usuarios, roles y login), `pedagogia` (categorías, asignaturas y recursos) y `core` (configuración).

## 2. Cómo se conecta la app

1. La app llama a `POST /api/token/` con `username` y `password` y recibe `access` y `refresh`.
2. En cada pedido siguiente manda el header `Authorization: Bearer <access>`.
3. Si el `access` vence (dura 1 día), pide uno nuevo con `POST /api/token/refresh/` (el `refresh` dura 7 días).
4. Todo va por **HTTPS**. La app tiene el tráfico sin cifrar prohibido en `network_security_config.xml`; la única excepción es `10.0.2.2` y `localhost` para el emulador.

Los datos de cada endpoint (qué recibe, qué devuelve, si pide token y qué rol lo puede usar) están en [ENDPOINTS.md](../api/ENDPOINTS.md).

## 3. Roles

| Rol | Qué puede |
|---|---|
| `admin` | Ve todos los recursos y los crea como plantillas base; ve y administra usuarios y cambia roles con `set-role` |
| `maquetador` | Ve todos los recursos; los que crea quedan como plantillas base y puede editar o borrar plantillas; edita su propio perfil |
| `asesor` | Ve las plantillas base y sus propios recursos; si edita una plantilla se le crea una copia personal y no puede borrar plantillas base; edita su propio perfil |

Son los únicos tres roles que existen. El rol `docente` no existe en el backend. El detalle fino de permisos por endpoint está en [ENDPOINTS.md](../api/ENDPOINTS.md).

El token lleva `is_staff` y `rol` como claims firmados, pero la app los usa solo para mostrar u ocultar opciones: **los permisos los decide siempre el servidor**.

## 4. Seguridad aplicada

| Medida | Estado |
|---|---|
| `DEBUG=False` | Por defecto |
| `SECRET_KEY` | Sale de una variable de entorno; si falta, el servidor no arranca |
| `.env` | No está en Git (`.gitignore`) |
| CORS | Cerrado, con lista explícita de orígenes |
| HTTPS | Obligatorio (`http://` redirige a `https://`) |
| Campos de privilegio (`is_staff`, `rol`, `is_active`) | Solo lectura en el serializer (Issue #18) |
| Claims del JWT | `is_staff` y `rol` dentro del token (Issue #19) |
| Cambio de rol | Solo un admin, con `POST /api/users/{id}/set-role/` |
| Registros de seguridad | Logins fallidos, accesos denegados y cambios de rol en `Backend/logs/security.log` |

Las pruebas de cada una, con el antes y el después, están en [CORRECCIONES_BACKEND.md](../seguridad/CORRECCIONES_BACKEND.md) y en la sección 5 de la [Wiki de Ciberseguridad](../../../../wiki/Ciberseguridad).

## 5. Variables de entorno

Van en el archivo `Backend/.env` del servidor (nunca en Git). Estos son los nombres; los valores no se publican.

| Variable | Para qué sirve |
|---|---|
| `SECRET_KEY` | Clave de firma de Django y de los tokens (obligatoria) |
| `DEBUG` | `False` en el servidor |
| `ALLOWED_HOSTS` | Dominio del servidor |
| `CSRF_TRUSTED_ORIGINS` | Origen HTTPS del servidor |
| `CORS_ALLOWED_ORIGINS` | Orígenes web permitidos (la app Android no usa CORS) |
| `DB_ENGINE` | `sqlite` o `mysql` |
| `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_HOST`, `DB_PORT` | Datos de MySQL (solo si se usa MySQL) |

## 6. Despliegue en PythonAnywhere

La guía paso a paso está en el repo del backend: [docs/DEPLOY_PYTHONANYWHERE.md](https://github.com/ISPC-TSDWAD/ModPWeb--Dev6/blob/develop/docs/DEPLOY_PYTHONANYWHERE.md). Resumen de lo que se hizo:

1. Clonar `ModPWeb--Dev6` en la consola Bash y crear el entorno virtual con Python 3.13.
2. Instalar `requirements.txt`.
3. Crear `Backend/.env` con las variables de la sección 5 y ponerle permiso `600`.
4. `python manage.py migrate` y `python manage.py collectstatic --noinput`.
5. Crear un superusuario propio con clave fuerte (la cuenta de prueba `admin` del README original ya no existe).
6. En la pestaña **Web**: apuntar el WSGI al proyecto, definir los archivos estáticos y activar **Force HTTPS**.
7. Apretar **Reload** y probar la URL.

Cosas a tener en cuenta:

- **Reload.** Después de cualquier cambio de código o de `.env` hay que apretar el botón *Reload* de la pestaña Web.
- **Vencimiento.** En el plan gratuito el sitio se desactiva si no se renueva. Hay que apretar *Run until 1 month from today* en la pestaña Web antes de la fecha de vencimiento (12/11/2026).
- **Rama.** El servidor corre la rama `feature/jonathan-guillen` del backend. Cuando se mergee el PR #64, se pasa a la rama integrada con `git checkout <rama>`, `git pull` y *Reload*.
- **Datos.** No ejecutar `reset_db.py` en el servidor: borra toda la base.
- **Claves de prueba.** Las cuentas de demostración (una por rol) se entregan por privado; no se publican en el repo.

## 7. Probar el backend

Con el servidor ya levantado, desde cualquier terminal:

```bash
BASE="https://dev6.pythonanywhere.com"

# Sin token tiene que devolver 401
curl -i "$BASE/api/pedagogia/recursos/"

# Login (usar una cuenta de prueba)
curl -s -X POST "$BASE/api/token/" -H "Content-Type: application/json" \
     -d '{"username":"<usuario>","password":"<clave>"}'
```

Pruebas automáticas del backend (desde la carpeta `Backend/` del otro repo):

```bash
export SECRET_KEY=cualquier-valor-de-prueba DEBUG=True DB_ENGINE=sqlite
python manage.py test users pedagogia      # 26 tests
```

Pruebas unitarias de la app: [docs/pruebas/PRUEBAS_UNITARIAS.md](../pruebas/PRUEBAS_UNITARIAS.md).
