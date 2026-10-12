# API EduTools — Lista de endpoints

Backend oficial: **ModPWeb--Dev6 / Backend** (Django REST Framework + SimpleJWT; MySQL en local, SQLite en el servidor de pruebas).
Las respuestas de esta página fueron obtenidas ejecutando el backend real (no son inventadas).

## Base URL

```
https://dev6.pythonanywhere.com
```

> Servidor público de pruebas (Issue #32). Cuenta gratuita de PythonAnywhere: usa SQLite y se renueva manualmente cada mes.
> La app Android solo acepta **HTTPS** (cleartext deshabilitado).

## Reglas generales

| Tema | Detalle |
|---|---|
| Formato | JSON. Enviar `Content-Type: application/json`. |
| Barra final | Todas las rutas **terminan en `/`**. Sin la barra, el servidor puede redirigir y perder el cuerpo del POST. |
| Autenticación | Header `Authorization: Bearer <access_token>` en todo endpoint marcado con 🔒. |
| Vida de los tokens | `access`: 1 día · `refresh`: 7 días. |
| Roles | Exactamente tres: `admin`, `asesor`, `maquetador`. |
| Idioma de errores | Español (excepto el de login incorrecto, que viene en inglés — ver abajo). |

### Errores comunes

| Código | Cuándo | Cuerpo |
|---|---|---|
| 401 | Falta el token o está vencido | `{"detail": "Las credenciales de autenticación no se proveyeron."}` |
| 403 | Token válido pero sin permiso para esa acción | `{"detail": "Usted no tiene permiso para realizar esta acción."}` |
| 400 | Datos inválidos | `{"campo": ["Este campo es requerido."]}` |
| 404 | El recurso no existe (o no es visible para tu rol) | `{"detail": "No encontrado."}` |

---

## 1. Autenticación

| Método | Ruta | Token | Recibe | Devuelve |
|---|---|---|---|---|
| POST | `/api/token/` | No | `username`, `password` | `refresh`, `access`, `is_staff`, `rol` |
| POST | `/api/token/refresh/` | No | `refresh` | `access` |

### POST `/api/token/` — iniciar sesión

Request:
```json
{ "username": "ana", "password": "<clave>" }
```

Respuesta `200`:
```json
{
  "refresh": "<jwt_refresh>",
  "access": "<jwt_access>",
  "is_staff": false,
  "rol": "asesor"
}
```

El `access` es un JWT firmado que además lleva dentro los claims `is_staff` y `rol` (Issue #19). La app puede leerlos del token (el payload es la parte central, Base64), pero **la autoridad es siempre el servidor**: cada pedido se valida con la firma.

Payload del `access` (ejemplo, sin datos reales):
```json
{ "token_type": "access", "user_id": 1, "is_staff": false, "rol": "asesor" }
```
(más `exp`, `iat` y `jti`, que se omiten acá)

Credenciales incorrectas → `401`:
```json
{ "detail": "No active account found with the given credentials" }
```

### POST `/api/token/refresh/` — renovar el access

Request: `{ "refresh": "<jwt_refresh>" }` → `200`: `{ "access": "<jwt_access>" }`

---

## 2. Usuarios — `/api/users/`

Objeto Usuario (lo que devuelve la API; la contraseña nunca se devuelve):

```json
{
  "id": 2,
  "username": "ana",
  "email": "ana@ejemplo.com",
  "first_name": "Ana",
  "last_name": "Lopez",
  "is_staff": false,
  "is_active": true,
  "rol": "asesor"
}
```

| Método | Ruta | Token | Quién puede | Recibe | Devuelve |
|---|---|---|---|---|---|
| GET | `/api/users/me/` | 🔒 | Cualquier autenticado | — | Tu usuario |
| PATCH | `/api/users/me/` | 🔒 | Cualquier autenticado | `first_name`, `last_name`, `email` (otros campos se ignoran) | Tu usuario actualizado |
| POST | `/api/users/change-password/` | 🔒 | Cualquier autenticado | `actual`, `nueva` (mín. 6 caracteres) | `{"detail": "Contraseña actualizada correctamente."}` |
| GET | `/api/users/` | 🔒 | Solo `is_staff` (admin) | — | Lista de usuarios |
| POST | `/api/users/` | 🔒 | Solo `is_staff` (admin) | `username`, `password`, `email`, `first_name`, `last_name`, `rol` | `201` + usuario creado |
| GET | `/api/users/{id}/` | 🔒 | Cualquier autenticado | — | Usuario |
| PUT / PATCH | `/api/users/{id}/` | 🔒 | Admin, o el propio usuario | Campos del usuario | Usuario actualizado |
| DELETE | `/api/users/{id}/` | 🔒 | Admin, o el propio usuario | — | `204` |
| POST | `/api/users/{id}/set-role/` | 🔒 | Solo `is_staff` (admin) | `rol`: `admin`, `asesor` o `maquetador` | Usuario con el rol nuevo |

Notas:
- `is_staff`, `is_active` y `rol` son **solo lectura** (Issue #18): si se envían en un `POST`/`PUT`/`PATCH` el servidor los ignora. El único camino para cambiar un rol es `POST /api/users/{id}/set-role/` (solo admin), que además deja registro en el log de seguridad.
- Un usuario creado con `POST /api/users/` nace con rol `asesor`; el admin lo cambia después con `set-role`.
- `set-role` con rol `admin` también marca `is_staff=true`; con cualquier otro rol, `false`. No se puede cambiar el rol de un superusuario ni quitarse a uno mismo el rol de admin (`400`).
- Un usuario no admin que intenta `GET /api/users/` o `POST /api/users/` recibe `403`.
- Un usuario no admin que modifica a **otro** usuario recibe `403`.
- Cambio de contraseña con la actual incorrecta → `400`: `{"detail": "La contraseña actual es incorrecta."}`

### POST `/api/users/{id}/set-role/` — cambiar el rol (solo admin)

Request:
```json
{ "rol": "maquetador" }
```

Respuesta `200`:
```json
{ "id": 2, "username": "ase_demo", "email": "", "first_name": "", "last_name": "",
  "is_staff": false, "is_active": true, "rol": "maquetador" }
```

Rol inexistente (por ejemplo `docente`) → `400`:
```json
{ "rol": ["\"docente\" no es una elección válida."] }
```

Usuario que no es admin → `403`:
```json
{ "detail": "Usted no tiene permiso para realizar esta acción." }
```

---

## 3. Pedagogía — `/api/pedagogia/`

Todos los endpoints de esta sección requieren 🔒 token (salvo la raíz, ver nota).

### 3.1 Categorías — `/api/pedagogia/categorias/`

```json
{
  "id": 2, "nombre": "ORGANIZADOR", "descripcion": "Organizadores",
  "activa": true,
  "creado_en": "2026-10-12T11:25:28.712158-03:00",
  "actualizado_en": "2026-10-12T11:25:28.712206-03:00"
}
```

| Método | Ruta | Recibe | Devuelve |
|---|---|---|---|
| GET | `/api/pedagogia/categorias/` | — | Lista |
| POST | `/api/pedagogia/categorias/` | `nombre` (único, obligatorio), `descripcion`, `activa` | `201` + categoría |
| GET | `/api/pedagogia/categorias/{id}/` | — | Categoría |
| PUT / PATCH | `/api/pedagogia/categorias/{id}/` | Campos de la categoría | Categoría actualizada |
| DELETE | `/api/pedagogia/categorias/{id}/` | — | `204` (no se puede borrar si tiene recursos asociados) |

### 3.2 Asignaturas — `/api/pedagogia/asignaturas/`

Mismo formato y mismos métodos que Categorías (campos: `id`, `nombre`, `descripcion`, `activa`, `creado_en`, `actualizado_en`).

| Método | Ruta |
|---|---|
| GET / POST | `/api/pedagogia/asignaturas/` |
| GET / PUT / PATCH / DELETE | `/api/pedagogia/asignaturas/{id}/` |

### 3.3 Recursos — `/api/pedagogia/recursos/`

```json
{
  "id": 1,
  "titulo": "Acordeón",
  "descripcion": "",
  "tipo": "acordeon",
  "contenido": "<div>...</div>",
  "url": "",
  "categoria": 1,
  "asignatura": 1,
  "categoria_nombre": "CTA",
  "asignatura_nombre": "Historia",
  "creado_por": 1,
  "es_plantilla_base": true,
  "copia_de": null,
  "activo": true,
  "creado_en": "2026-10-12T11:25:28.720524-03:00",
  "actualizado_en": "2026-10-12T11:25:28.720549-03:00"
}
```

Valores válidos de `tipo`: `video`, `acordeon`, `infografia`, `cuestionario`, `lectura`, `actividad`.

| Método | Ruta | Recibe | Devuelve |
|---|---|---|---|
| GET | `/api/pedagogia/recursos/` | — | Lista (filtrada por rol, ver abajo) |
| POST | `/api/pedagogia/recursos/` | `titulo`, `tipo`, `categoria` (id), `asignatura` (id); opcionales: `descripcion`, `contenido`, `url`, `activo` | `201` + recurso |
| GET | `/api/pedagogia/recursos/{id}/` | — | Recurso |
| PUT / PATCH | `/api/pedagogia/recursos/{id}/` | Campos del recurso | Recurso actualizado (ver regla del asesor) |
| DELETE | `/api/pedagogia/recursos/{id}/` | — | `204` |

Reglas por rol:

| Rol | Qué ve | Al crear | Al editar una plantilla base | Al borrar una plantilla base |
|---|---|---|---|---|
| `admin` / `maquetador` | Todos los recursos | Se guarda como **plantilla base** (`es_plantilla_base: true`) | Edita la plantilla | Permitido |
| `asesor` | Plantillas base + sus propios recursos | Se guarda como recurso propio (`es_plantilla_base: false`) | **No modifica la plantilla: se crea una copia personal** (`201`, título `"… (Mi Copia)"`, `copia_de` = id original) | `403`: `"No tienes permiso para eliminar una plantilla base institucional."` |

`creado_por` lo asigna el servidor (no se envía).

---

## 4. Otras rutas

| Método | Ruta | Token | Nota |
|---|---|---|---|
| GET | `/api/pedagogia/` | No | Índice público de DRF con los links a categorías/asignaturas/recursos. No devuelve datos. |
| GET | `/` | — | Redirige a `/api/pedagogia/recursos/` (devuelve `401` sin token). |
| — | `/admin/` | — | Panel de administración de Django (no es parte de la API de la app). |

---

## 5. Probar rápido con curl

```bash
BASE="https://dev6.pythonanywhere.com"

# 1) Login (guardar el access)
curl -s -X POST "$BASE/api/token/" -H "Content-Type: application/json" \
     -d '{"username":"<usuario>","password":"<clave>"}'

# 2) Usar el token
curl -s "$BASE/api/pedagogia/recursos/" -H "Authorization: Bearer <access>"
```

## 6. Desde la app Android (Java)

- Header en cada request autenticado: `Authorization: Bearer <access>`.
- Si una respuesta es `401`, renovar con `POST /api/token/refresh/`; si el refresh también vence, volver al login.
- Guardar los tokens en almacenamiento seguro (no en logs ni en el código).
