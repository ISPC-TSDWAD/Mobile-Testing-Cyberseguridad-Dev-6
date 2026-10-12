# Guion del video de seguridad

Video para Ciberseguridad (Sprint 2). Dura unos 4 minutos. Lo graban Dani y Jonathan: Jonathan maneja la app y explica el código, Dani graba la pantalla y edita.

Qué tiene que mostrar el video (lo que pide la cátedra): **registro, login, acceso a algo protegido, qué roles hay y cómo el backend deniega una acción**.

## Antes de grabar

- Servidor de PythonAnywhere en `main` y recargado. Probar `https://dev6.pythonanywhere.com/api/users/`: tiene que devolver `401` sin token.
- Dos usuarios listos: un **admin** y un **asesor** (el asesor se crea desde la app con el botón "+", nace con rol asesor).
- Emulador o celular con datos móviles y la app instalada, con sesión cerrada.
- En PythonAnywhere, abrir una consola Bash en `~/ModPWeb--Dev6/Backend` para mostrar el log de seguridad.
- No mostrar contraseñas en pantalla: tapar el campo o usar un usuario de prueba.

## Escenas

| # | Tiempo | Qué se muestra | Qué se dice |
|---|---|---|---|
| 1 | 0:00 | Pantalla de login de la app | "Esta es EduTools Mobile. La app habla solo HTTPS con el backend de Django." |
| 2 | 0:20 | Login del admin con la contraseña incorrecta → mensaje de error | "Con credenciales malas el backend responde 401 y la app muestra el mensaje, sin guardar ningún token." |
| 3 | 0:40 | Login del admin correcto → Home con "Rol: Administrador" | "El backend devuelve un token JWT. La app lo guarda con `SessionManager` y lee el rol de adentro del token con `JwtUtil`." |
| 4 | 1:10 | Usuarios → se ve la lista y el botón "+" | "Solo el admin puede listar usuarios y ver el botón. Esto se oculta en la app, pero la seguridad de verdad la hace el servidor." |
| 5 | 1:30 | Botón "+" → Registro de un usuario nuevo → aparece en la lista | "El alta va por `POST api/users/`. El rol no se manda: el servidor lo ignora (Issue #18) y el usuario nace como asesor." |
| 6 | 2:00 | Cerrar sesión y entrar con el asesor → Home con "Rol: Asesor Pedagógico" | "Los roles son tres: admin, asesor y maquetador. Cada uno entra con su token." |
| 7 | 2:20 | Asesor entra a Usuarios → mensaje "No tenés permiso para esta acción", sin botón "+" | "Acá el backend deniega: un asesor recibe 403 al pedir la lista de usuarios." |
| 8 | 2:45 | Consola de PythonAnywhere: `tail -n 20 logs/security.log` | "Cada acceso denegado y cada cambio de rol queda registrado con usuario, ruta y resultado. Nunca se guardan contraseñas ni tokens." |
| 9 | 3:10 | Terminal: `python manage.py test users.tests_seguridad` | "Y está probado: un asesor que intenta `set-role`, incluso sobre sí mismo, recibe 403 y su rol no cambia. Son 27 tests en total." |
| 10 | 3:40 | Wiki de Ciberseguridad (sección 5) | "El detalle de cada corrección está documentado en la Wiki, con el antes y el después." |

## Para tener a mano si preguntan

- **¿Dónde se guarda el token?** En `SessionManager`, hoy en memoria (`MemoryTokenStorage`). Pasar a almacenamiento cifrado es el Issue #26.
- **¿Se puede cambiar el rol desde la app?** No. El único camino es `POST /api/users/{id}/set-role/`, solo admin, y queda en el log.
- **¿Qué pasa si alguien edita el token?** Los claims `rol` e `is_staff` van firmados: si se tocan, el servidor rechaza el token (401).
- **¿Por qué la app oculta el botón si el servidor ya valida?** Es solo comodidad para el usuario. La app nunca decide permisos.
