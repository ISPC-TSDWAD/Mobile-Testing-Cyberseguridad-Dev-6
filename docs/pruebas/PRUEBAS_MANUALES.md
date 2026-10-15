# Pruebas manuales en dispositivo (Issue #25)

Se probó la app en un **Samsung SM-A546E** (instalada desde Android Studio) contra el backend publicado en `https://dev6.pythonanywhere.com/`, usando datos móviles (fuera de la red del equipo). Para la prueba se crearon dos usuarios de prueba en el servidor: `admin_prueba` (rol admin) y `asesor_prueba` (rol asesor).

| # | Caso | Pasos | Resultado esperado | Resultado |
|---|------|-------|--------------------|-----------|
| 1 | Pantalla de login | Abrir la app | Se ve el formulario de login | OK |
| 2 | Login como admin | Ingresar con `admin_prueba` | Pasa al Home con "Rol: Administrador"; en Usuarios se ve el botón "+" | OK |
| 3 | Login como asesor | Cerrar sesión e ingresar con `asesor_prueba` | Pasa al Home con "Rol: Asesor Pedagógico"; en Usuarios no hay "+" y la lista muestra el 403 ("No tenés permiso para esta acción") | OK |
| 4 | Credenciales inválidas | Ingresar con una contraseña incorrecta | Mensaje "Sesión vencida o credenciales inválidas" (401), sin guardar sesión | OK |
| 5 | Datos móviles | Repetir el login con el wifi apagado | El login funciona contra el servidor público | OK |
| 6 | Cierre de sesión | Tocar "Cerrar sesión" | Vuelve al login y se borran los tokens | OK |
| 7 | Contraseña débil en Registro | Entrar como admin, Usuarios, botón "+" y en Registro escribir `abcdefghi` | El campo muestra lo que falta ("Agregá al menos un carácter especial…", "Agregá al menos un número.") y no se envía | OK: no dejó registrar hasta cumplir los requisitos, pidiendo uno por vez |
| 8 | La sesión sobrevive al cierre | Iniciar sesión, cerrar la app desde recientes y abrirla de nuevo | Entra directo al Home con el mismo rol (token en `EncryptedSharedPreferences`) | OK |
| 9 | Cerrar sesión borra la sesión guardada | Tocar "Cerrar sesión", cerrar la app y abrirla | Aparece el login | OK |

## Selector de tema y vista previa de recursos (pendiente de probar en el celular)

Estos casos corresponden a la rama `feature/tema-y-previsualizacion`. Los tests automáticos están en [PRUEBAS_UNITARIAS.md](PRUEBAS_UNITARIAS.md); lo que sigue se completa al instalar la app en el celular. Cada caso se repite en tema **Claro** y **Oscuro**.

| # | Caso | Pasos | Resultado esperado | Resultado |
|---|------|-------|--------------------|-----------|
| 10 | Elegir tema | Home, tarjeta "Apariencia", elegir Oscuro | Toda la app pasa a oscuro y la tarjeta dice "Tema: Oscuro" | Pendiente |
| 11 | Tema Claro y Según el sistema | Elegir Claro; luego Según el sistema con el celular en oscuro | Claro fuerza el claro; "Según el sistema" sigue al celular | Pendiente |
| 12 | El tema se recuerda | Elegir Oscuro, cerrar la app desde recientes y abrirla | Login y Home abren en oscuro | Pendiente |
| 13 | Todas las pantallas | Recorrer Recursos, Detalle, Formulario, Usuarios, Perfil, Contacto, Multimedia y Quiénes somos | Todas respetan el tema elegido, sin texto ilegible | Pendiente |
| 14 | Recurso con contenido HTML | Abrir un recurso con `contenido` (por ejemplo una lectura) | La sección "Contenido" muestra el HTML con los colores del tema | Pendiente |
| 15 | Recurso con enlace HTTPS | Abrir un recurso con URL `https://` | Aparece el botón "Abrir enlace" (o "Abrir video") y el aviso "Se abre en tu navegador, fuera de EduTools"; al tocarlo pide confirmar y abre el navegador | Pendiente |
| 16 | Recurso sin contenido ni enlace | Abrir un recurso sin `contenido` ni `url` | Mensaje "Este recurso todavía no tiene contenido ni enlace para mostrar" | Pendiente |
| 17 | Enlaces dentro del HTML | En un HTML con un enlace `https://`, tocarlo | No navega dentro de la vista: pide confirmar y abre afuera | Pendiente |
| 18 | Editar o copiar una plantilla | Como asesor, editar una plantilla base (título, descripción, URL) y guardar; abrir la copia | La copia tiene los cambios y conserva el contenido; la plantilla base no cambió | Pendiente |

## Capturas

| Caso | Captura |
|------|---------|
| Login | ![Login](capturas/01-login.jpg) |
| Admin en el Home | ![Home admin](capturas/02-home-admin.jpg) |
| Asesor en el Home | ![Home asesor](capturas/03-home-asesor.jpg) |
| Contraseña incorrecta (401) | ![Error 401](capturas/04-login-error-401.jpg) |

## Notas

- La seguridad real la aplica el servidor: la app solo usa el rol del token para mostrar u ocultar botones. Un asesor que llame a `POST api/users/{id}/set-role/` recibe 403 (cubierto por `users/tests_seguridad.py` en el backend).
- Los tests automáticos de esta parte están en [PRUEBAS_UNITARIAS.md](PRUEBAS_UNITARIAS.md).
