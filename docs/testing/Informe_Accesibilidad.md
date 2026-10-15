# Informe de Accesibilidad — EduTools Mobile, Sprint 2

Actualización: 14/10/2026. Equipo activo: Gonzalo Velasco, Jonathan Guillén, Gerardo Romero y Roni Duncan. Referencia: pautas de Android y WCAG 2.2 AA aplicables a móvil. Casos: TC-18, TC-19, TC-20, TC-23 y TC-24; historia HUM-09.

## Evidencias previas de Jonathan

La auditoría con Accessibility Scanner del Samsung SM-A546E se conserva en [el informe original](../accesibilidad/INFORME_ACCESIBILIDAD.md). Documentó tres hallazgos: texto repetido en Perfil, contraste del hint del formulario y altura fija de Toolbar. Fueron corregidos; el reescaneo no mostró sugerencias. Se agruparon en el [Issue #39](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/39).

Estas evidencias son de Jonathan. No se presentan como auditoría propia de Roni, Gonzalo o Gerardo.

## Revisión de Gonzalo del 14/10

Dispositivo: Motorola moto g85 5G, Android 16. Se recorrió la app mediante controles Android, se inspeccionó su árbol accesible y se capturaron pantallas con fuente 200%. Se usaron cuentas de prueba y un recurso temporal que fue eliminado al terminar. Se restauraron fuente, autocompletado, animaciones y pantalla encendida.

Se revisaron Login, Home, Recursos vacío y con una fila, Detalle, Formulario de recurso, Usuarios, Perfil del usuario de prueba, Registro, Contacto, Multimedia y Quiénes somos. La revisión visual no certifica el audio de TalkBack ni una corrida de Accessibility Scanner. TalkBack está instalado; Scanner no se detectó en este dispositivo.

| Hallazgo | Pauta | Severidad | Evidencia y corrección | Estado |
|---|---|---|---|---|
| ACC-GV-01: títulos de sección sin propiedad de encabezado | Estructura, WCAG 1.3.1 / navegación por encabezados | Media | Revisión de layouts y BaseActivity; se agregaron etiquetas de encabezado compatibles con minSdk 21 mediante ViewCompat. TC-23 verifica Home | Corregido y test aprobado; registro como Issue pendiente |
| ACC-GV-02: resultados dinámicos de Recursos y Usuarios sin región de anuncio | Cambios dinámicos, WCAG 4.1.3 | Media | Cantidades y estado no tenían live region. Se agregó `polite` a los tres TextView; TC-24 verifica el metadato | Corregido y test aprobado; escucha e Issue pendientes |
| ACC-GV-03: etiqueta de usuario cortada con fuente 200% | Texto ampliado, WCAG 1.4.4 | Media | [Antes](capturas/21-login-fuente-2.png) y [después](capturas/22-login-fuente-2-corregido.png). Se cambió a “Nombre de usuario” | Corregido y verificado visualmente; Issue pendiente |

## Chequeos automatizados

La [corrida final de Espresso](../pruebas/EVIDENCIA_ESPRESSO.md) dio **7 tests, 0 fallas**, en 23,533 segundos. AccessibilityChecks estuvo activo sin supresiones. Los tres flujos críticos siguen siendo Login, Contacto y Registro; los dos tests adicionales comprueban encabezados y metadatos de anuncios.

Se conservaron [fallos de entorno previos](../pruebas/evidencias/espresso-fallo-autocompletado-13-10.txt): el administrador de contraseñas quitó el foco. Se preparó el dispositivo y se repitió con éxito, restaurando después sus ajustes. Esto no se registra como bug de accesibilidad de la app.

## Resultados de inspección y revisión visual

| Verificación | Resultado | Evidencia |
|---|---|---|
| Etiquetas de Login | Usuario y contraseña están identificados; el botón del ojo tiene “Mostrar contraseña” | Árbol accesible del dispositivo y capturas de Login |
| Errores por campo | Ambos campos vacíos muestran “Este campo es obligatorio” y conservan sus etiquetas | [Login con errores](capturas/02-login-errores.png); Espresso |
| Recorrido Login → Home → Recursos → Detalle | Realizado mediante controles Android con cuenta de asesor y recurso temporal | Capturas y árbol accesible; no se escuchó TalkBack |
| Fuente 200% | Pantallas principales recorridas; se corrigió la etiqueta cortada de Login. Los formularios y tarjetas usan desplazamiento vertical | [Registro](capturas/15-registro-fuente-2.png), [Formulario](capturas/18-formulario-fuente-2.png), [Contacto](capturas/09-contacto-fuente-2.png) |
| Detalle del recurso con texto ampliado | Título completo visible en el cuerpo; Toolbar puede abreviarlo | [Detalle](capturas/17-detalle-fuente-2.png) |
| Multimedia y Quiénes somos | Textos ampliados y contenido desplazable revisados | [Multimedia](capturas/19-multimedia-fuente-2.png), [Quiénes somos](capturas/20-acerca-fuente-2.png) |
| Estado de usuarios | Se muestra Activo/Inactivo como texto, además del color | Revisión de Usuarios y evidencia previa |
| Encabezados | Metadato verificado por TC-23 | [Reporte instrumentado](../pruebas/evidencias/espresso-14-10.html) |
| Regiones de anuncio | `polite` verificado por TC-24 | Mismo reporte; audio pendiente |
| Contraste y área táctil | Chequeos Espresso aprobados en los flujos instrumentados; auditoría Scanner previa de Jonathan | No se extiende el resultado a todas las pantallas sin Scanner |

## Escucha de TalkBack — pendiente

No se registran anuncios escuchados que nadie haya verificado. Falta recorrer con TalkBack activo Login, Home, Recursos y Detalle, anotando las frases anunciadas, el orden real de lectura y si los errores y cambios dinámicos se anuncian.

Activar en Ajustes → Accesibilidad → TalkBack → Usar TalkBack. Un toque selecciona y anuncia; doble toque activa; dos dedos desplazan. [Instrucciones oficiales](https://support.google.com/accessibility/android/answer/6007100?hl=es).

| Paso | Evidencia por registrar | Estado |
|---|---|---|
| Campos de Login y errores | Qué anuncia en cada campo y al validar vacío | Pendiente de escucha |
| Home | Orden de tarjetas y navegación por encabezados | Pendiente de escucha |
| Recursos | Anuncio de fila completa y cambios de estado/cantidad | Pendiente de escucha |
| Detalle | Títulos, etiquetas y botones accesibles | Pendiente de escucha |

## Pendientes para cerrar accesibilidad

- Ejecutar Scanner en las pantallas principales restantes y conservar sus capturas o justificar ausencia de hallazgos.
- Completar la escucha de TalkBack y comprobar foco en diálogos, orientación horizontal y tamaño de pantalla máximo del sistema.
- Registrar como Issues los tres hallazgos nuevos y verificar su estado al integrar las correcciones.
- Incorporar los aportes individuales reales de Roni y Gerardo. Las tareas asignadas y la documentación consolidada no sustituyen sus ejecuciones o hallazgos propios.
- Ejecutar compatibilidad en API 21/22. Las pruebas de este dispositivo solo acreditan Android 16.

Las credenciales y los árboles completos con datos de cuentas se conservaron solo en archivos privados ignorados por Git. Las capturas publicables contienen formularios vacíos y el recurso temporal de la revisión.
