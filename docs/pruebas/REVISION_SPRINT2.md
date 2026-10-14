# Revisión de cierre — Sprint 2 / Evidencia 3

Fecha: 13/10/2026. Rama de trabajo: `feature/gonzalo-velasco`, actualizada hasta `origin/develop` (`b3627e0`).

Requisitos contrastados con el [documento ABP compartido por el equipo](https://docs.google.com/document/d/12Zcq7IckMen_Gnz_A1XAZzsOe7gxziNKL1pWvUBdCUQ/edit?tab=t.0). Entrega: **14/10/2026**, confirmada por el equipo. No se da por finalizada la entrega hasta que el código esté en `main` y las evidencias sean accesibles.

## Cambios realizados

- Contacto se presenta como demostración: valida, avisa que no envió ni guardó la consulta y conserva los campos. Se eliminó la promesa de respuesta en 48 horas y el correo ficticio, incluido el botón para copiarlo en Quiénes somos.
- El formulario de Recursos deja de inventar categoría/asignatura `1`. Si no dispone de IDs válidos, no envía el recurso, informa el problema y reintenta los catálogos. En edición puede conservar los IDs reales del recurso recibido.
- Todas las llamadas protegidas de Recursos, Usuarios, Perfil y Registro usan `CallbackConSesion` de `BaseActivity`, incluidos ambos catálogos. Un 401 borra la sesión y limpia la navegación; un 403 conserva la sesión y muestra falta de permisos. Los callbacks ignoran Activities destruidas y llamadas canceladas.
- La renovación automática del access sigue pendiente. Se documenta que hay que volver a iniciar sesión al vencerlo. No se implementó el endpoint de refresh que el traspaso proponía: la documentación de la API identifica `/api/token/refresh/`, no `/api/users/token/refresh/`.
- README actualizado: CRUD de recursos, video incluido, 11 Activities, enlace a la planilla y estado real de Contacto.
- Workflow de GitHub Actions preparado para tests JVM, cobertura, lint y compilación de los APK de app y de tests, con artifacts. No ejecuta Espresso.
- Se corrigió un error existente en `LoginFlujoTest`: el tipo genérico de `ApplicationProvider.getApplicationContext()` hacía ambiguo el constructor de `SessionManager`. Se indica explícitamente `Context`.

## Verificación ejecutada

Comando en Windows:

```powershell
.\gradlew.bat testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest
```

| Comprobación | Resultado | Evidencia local |
|---|---|---|
| Tests JVM | 87 tests, 0 fallas, 0 errores | `app/build/reports/tests/testDebugUnitTest/index.html` y XML en `app/build/test-results/testDebugUnitTest/` |
| Integración simulada de red | 15 tests incluidos en los 87 (AuthRepository, UsuarioApi y RecursoApi) | Reporte JUnit; MockWebServer, sin tocar el servidor publicado |
| Cobertura JVM | 282/1039 líneas: 27,1% | `app/build/reports/jacoco/jacocoTestReport/html/index.html` y `jacocoTestReport.xml` |
| Lint Android | 0 errores, 45 advertencias | `app/build/reports/lint-results-debug.html` |
| APK app | Compilación correcta | `app/build/outputs/apk/debug/app-debug.apk` |
| APK de tests instrumentados | Compilación correcta; 5 tests en 3 flujos | `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk` |
| Dispositivo/emulador | Ninguno conectado; no se detectaron AVD ni imágenes de sistema instaladas | Consulta local de ADB y del SDK |

El porcentaje de cobertura incluye Activities y adapters. No se excluyen para mejorar la cifra; los tests JVM no ejercitan las pantallas ni el Keystore. El reporte no mide la ejecución de Espresso.

## Trazabilidad de los nuevos tests

Los casos complementarios están en [CASOS_ROBUSTEZ.csv](CASOS_ROBUSTEZ.csv). Se conserva la planilla local de Roni sin adjudicar sus cambios a esta revisión.

| Historia | Caso | Test | Resultado de esta revisión | Hallazgo |
|---|---|---|---|---|
| HUM-11 Seguridad del acceso | TC-11 | `ApiCallbackTest#respuesta401_resuelta_noMuestraOtroError` | Pasó | 401 resuelto sin segundo mensaje de error |
| HUM-11 Seguridad del acceso | TC-11 | `ApiCallbackTest#respuesta401_sinRedireccion_conservaMensajeDeError` | Pasó | Un cliente sin redirección conserva el error de autenticación |
| HUM-06 Usuarios | SEC-02 | `ApiCallbackTest#respuesta403_noCierraSesion` | Pasó | Falta de permisos no equivale a sesión vencida |
| HUM-12 CRUD de recursos | TC-07 | `ApiCallbackTest#respuesta204_esExitoSinCuerpo` | Pasó | Eliminación exitosa sin cuerpo JSON |
| HUM-08 Contacto | TC-21 | `ContactoFlujoTest#contactoValido_conservaConsultaEnPantallaSinEnviar` | Compilado; pendiente de ejecución | Antes cerraba la pantalla anunciando un envío inexistente |
| HUM-12 CRUD de recursos | TC-22 | Manual: alta con catálogo no disponible | Pendiente de ejecución | Antes enviaba IDs 1 aunque no hubiera selección real |

Los hallazgos de Contacto y catálogos todavía deben vincularse a Issues si el equipo decide registrarlos. Los 4 tests JVM verifican la política del callback; no sustituyen una prueba de navegación real ante 401.

## Pendientes antes de entregar

| Pendiente | Responsable según el traspaso/issues | Qué falta verificar |
|---|---|---|
| Publicar esta revisión y ejecutar CI | Gonzalo | PR de esta rama a `develop`, revisión y ejecución remota del workflow; luego integración a `main` |
| Cambios locales de Jhony | Jonathan | App `116c56e`/`752bf7f`, backend `3ef16be` y Wiki `394ce22` no se encuentran en este checkout. Confirmar push/PR, integración y reload del backend |
| Testing local de Roni | Roni / Gonzalo | Hay diferencias locales respecto de `develop` en `docs/testing/` y `tests/api/`, y la planilla CSV está sin versionar. Revisarlas y publicar los archivos acordados sin perder ese trabajo |
| Postman/Newman | Roni, [#27](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/27) | Ejecutar con credenciales de prueba fuera del repositorio, guardar reporte sanitizado y resultado real; no se ejecutó en esta revisión |
| Accesibilidad | Roni, [#28](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/28) | TalkBack en flujo completo, fuente y pantalla máximas, orientación y revisión de las pantallas principales. Tres hallazgos ya documentados en #39 no acreditan por sí solos la participación individual requerida |
| Compatibilidad Android 5 | Equipo | Ejecutar en API 21/22 y comprobar almacenamiento en memoria, cierre de proceso y ausencia de crashes |
| Espresso | Gonzalo, [#40](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/40) | Reejecutar los 5 tests con AccessibilityChecks. La evidencia del 12/10 documenta 4 tests previos, no la nueva suite |
| Wiki de Testing | Roni / Gonzalo | Enlazar planilla, matriz, informe, cobertura, reportes y Actions; justificar UI Automator si no hay interacción con apps externas/permisos |
| Wiki de Ciberseguridad | Jonathan, [#30](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/30) | Responsables y frecuencia por política, relación con requisitos, permisos por rol, logs y evidencias reales de backend |
| APK descargable / release | Gonzalo, [#31](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/31) | Publicar un APK accesible a los docentes y colocar enlace. El APK local está firmado para depuración; no se publicó una release |
| Demo grupal final | Todo el equipo, #31 | Video de hasta 3 minutos con participación del equipo; mostrar CRUD y autenticación/autorización, publicar enlace y verificar permisos de acceso |
| Proyecto / Scrum | Gonzalo | IEEE830 actualizado, Planning/Daily/Review/Retro con datos reales, milestone, Kanban y aportes individuales visibles |
| Cierre en main | Gonzalo | Integrar siguiendo feature → develop → main; cerrar issues solo con el PR final y evidencia verificada |

La consulta a GitHub del 13/10 no mostró PR abiertos en la app. Esta revisión no publicó cambios en GitHub, no modificó Wiki/backend y no desplegó PythonAnywhere.

## Verificación manual de las correcciones

1. Contacto: completar datos válidos y tocar **Validar consulta**. Debe informar que no se envió ni guardó, conservar los campos y no abrir otras apps. Con correo inválido, conservar el error por campo.
2. Recursos: sin conectividad, abrir un alta, completar título/URL y guardar. No debe enviar IDs inventados. Recuperar conexión, reintentar, revisar categoría/asignatura y guardar solo con valores reales.
3. Catálogos: simular 401 al cargar categorías o asignaturas. Debe cerrar sesión y abrir login una sola vez.
4. Usuarios, Registro, Perfil y Recursos: simular 401 en cada operación protegida. Debe volver al login sin permitir volver al área autenticada con Atrás. Un 403 debe conservar la sesión.
5. Login: credenciales incorrectas (401) deben mostrar error de acceso, sin tratarlo como vencimiento de sesión.

## Archivos locales preservados

Antes de incorporar `develop`, se respaldaron los archivos locales sin seguimiento en `app/build/respaldo-revision-sprint2/testing/` y `app/build/respaldo-revision-sprint2/tests/`. Después se restituyeron sus contenidos en las rutas originales. El respaldo también contiene el texto descargado del ABP. Es una carpeta ignorada por Git; conservarla hasta acordar las diferencias con Roni y no ejecutar una limpieza de build mientras se necesite.
