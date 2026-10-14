# Pruebas unitarias de la app (JUnit)

Son pruebas de lógica pura, sin emulador ni dispositivo. Corren en la JVM con JUnit 4.13.2.

## Cómo correrlas

Desde Android Studio: click derecho sobre `app/src/test` > *Run Tests*.

Por consola (desde la raíz del repo):

```
./gradlew testDebugUnitTest
```

El reporte queda en `app/build/reports/tests/testDebugUnitTest/index.html`.

## Qué se prueba

| Clase | Qué se verifica | Tests |
|---|---|---|
| `Validador` (`util/`) | email, usuario del login, título y URL del recurso | 19 |
| `Validador` — contraseña (`ValidadorPasswordTest`) | más de 8 caracteres, letra, número y carácter especial, con el mensaje de lo que falta | 8 |
| `UsuarioItem` (`model/`) | etiquetas legibles de rol y estado, getters | 6 |
| `Recurso` (`model/`) | etiquetas legibles del tipo de recurso, getters | 6 |
| `JwtUtil` (`util/`) | lee `rol` e `is_staff` del payload del token y su vencimiento (`exp`) | 10 |
| `SessionManager` (`security/`) | la sesión es válida solo con token sin vencer; cerrar sesión borra tokens y usuario | 5 |
| `AuthRepository` (`net/`) | login contra MockWebServer: 200 guarda los tokens, 401/500 y sin red muestran error | 5 |
| `UsuarioApi` (`net/`) | CRUD de usuarios contra MockWebServer: rutas, token, 403, 201 y 204 | 5 |

Los tests de `ApiErrorMapper` (14, incluidos el 429 del límite de intentos y el 409 de dato en uso), `AuthInterceptor` (3) y la capa de red de Gonzalo se suman a esta tabla en `app/src/test`.

Total: 81 tests, todos pasan (`testDebugUnitTest`, 13/10).

## Validador

| Método | Regla | Casos probados |
|---|---|---|
| `esEmailValido` | formato `algo@dominio.ext` | correcto, sin arroba, sin dominio, vacío, null, con espacios |
| `normalizarUsuario` | saca espacios; si queda vacío devuelve `invitado` | vacío, solo espacios, null, con espacios |
| `esTituloValido` | no vacío y hasta 200 caracteres (igual que el backend) | normal, vacío, null, 200 y 201 caracteres |
| `esUrlValida` | vacía vale (es opcional); si hay valor debe empezar con `https://` | https, http, vacía, null, sin dominio, con espacios, `javascript:` |

La regla de HTTPS viene de que la app tiene el tráfico sin cifrar deshabilitado, así que una URL `http://` no se podría abrir.

## Roles

Los únicos roles que existen en el backend son `admin`, `asesor` y `maquetador`. El rol "docente" no existe: `UsuarioItem.getRolLegible()` lo devuelve tal cual, sin etiqueta propia, y hay un test que lo deja asentado.

## Login y usuarios con la red (Issue #25)

Estas pruebas usan `MockWebServer`: un servidor falso que corre en la PC, así que no tocan el backend real ni necesitan internet.

| Test | Qué comprueba |
|---|---|
| `login200_guardaLosTokens` | la respuesta 200 deja `access` y `refresh` guardados en el `SessionManager` |
| `login_hacePostAApiTokenConBarraFinalYJson` | el pedido es `POST /api/token/` con el usuario en JSON |
| `login401_muestraErrorYNoGuardaNada` | con credenciales malas se muestra el mensaje de `ApiErrorMapper` y no se guarda ningún token |
| `login500_muestraErrorDelServidor` | error del servidor, sin token guardado |
| `sinConexion_muestraSinConexion` | si el servidor no responde se muestra "Sin conexión" |
| `listar_leeLosUsuariosYEnviaElToken` | `GET /api/users/` lee la lista y manda `Authorization: Bearer` |
| `listar403_...` | un asesor recibe 403 al listar usuarios |
| `crear_hacePostSinMandarRolNiIsStaff` | el alta no manda `rol` ni `is_staff` (el servidor los ignora, Issue #18) |
| `editar_...` / `eliminar_...` | `PATCH` y `DELETE` a `/api/users/{id}/` (con barra final) |

`JwtUtil` solo sirve para mostrar u ocultar botones (por ejemplo el "+" de Usuarios es solo para admin). La seguridad real la hace el servidor, que revisa la firma del token en cada pedido.

Los tests de `JwtUtil` arman tokens de prueba con firma falsa, porque `JwtUtil` no verifica la firma.

