# RevisiÃ³n de cierre â€” Sprint 2 / Evidencia 3

Fecha: 13/10/2026. Rama de trabajo: `feature/gonzalo-velasco`, actualizada hasta `origin/develop` (`b3627e0`).

Requisitos contrastados con el [documento ABP compartido por el equipo](https://docs.google.com/document/d/12Zcq7IckMen_Gnz_A1XAZzsOe7gxziNKL1pWvUBdCUQ/edit?tab=t.0). Entrega: **14/10/2026**, confirmada por el equipo. No se da por finalizada la entrega hasta que el cÃ³digo estÃ© en `main` y las evidencias sean accesibles.

## Cambios realizados

- Contacto se presenta como demostraciÃ³n: valida, avisa que no enviÃ³ ni guardÃ³ la consulta y conserva los campos. Se eliminÃ³ la promesa de respuesta en 48 horas y el correo ficticio, incluido el botÃ³n para copiarlo en QuiÃ©nes somos.
- El formulario de Recursos deja de inventar categorÃ­a/asignatura `1`. Si no dispone de IDs vÃ¡lidos, no envÃ­a el recurso, informa el problema y reintenta los catÃ¡logos. En ediciÃ³n puede conservar los IDs reales del recurso recibido.
- Todas las llamadas protegidas de Recursos, Usuarios, Perfil y Registro usan `CallbackConSesion` de `BaseActivity`, incluidos ambos catÃ¡logos. Un 401 borra la sesiÃ³n y limpia la navegaciÃ³n; un 403 conserva la sesiÃ³n y muestra falta de permisos. Los callbacks ignoran Activities destruidas y llamadas canceladas.
- La renovaciÃ³n automÃ¡tica del access sigue pendiente. Se documenta que hay que volver a iniciar sesiÃ³n al vencerlo. No se implementÃ³ el endpoint de refresh que el traspaso proponÃ­a: la documentaciÃ³n de la API identifica `/api/token/refresh/`, no `/api/users/token/refresh/`.
- README actualizado: CRUD de recursos, video incluido, 11 Activities, enlace a la planilla y estado real de Contacto.
- Workflow de GitHub Actions preparado para tests JVM, cobertura, lint y compilaciÃ³n de los APK de app y de tests, con artifacts. No ejecuta Espresso.
- Se corrigiÃ³ un error existente en `LoginFlujoTest`: el tipo genÃ©rico de `ApplicationProvider.getApplicationContext()` hacÃ­a ambiguo el constructor de `SessionManager`. Se indica explÃ­citamente `Context`.

## VerificaciÃ³n ejecutada

Comando en Windows:

```powershell
.\gradlew.bat testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest
```

| ComprobaciÃ³n | Resultado | Evidencia local |
|---|---|---|
| Tests JVM | 87 tests, 0 fallas, 0 errores | `app/build/reports/tests/testDebugUnitTest/index.html` y XML en `app/build/test-results/testDebugUnitTest/` |
| IntegraciÃ³n simulada de red | 15 tests incluidos en los 87 (AuthRepository, UsuarioApi y RecursoApi) | Reporte JUnit; MockWebServer, sin tocar el servidor publicado |
| Cobertura JVM | 282/1039 lÃ­neas: 27,1% | `app/build/reports/jacoco/jacocoTestReport/html/index.html` y `jacocoTestReport.xml` |
| Lint Android | 0 errores, 45 advertencias | `app/build/reports/lint-results-debug.html` |
| APK app | CompilaciÃ³n correcta | `app/build/outputs/apk/debug/app-debug.apk` |
| APK de tests instrumentados | CompilaciÃ³n correcta; 5 tests en 3 flujos | `app/build/outputs/apk/androidTest/debug/app-debug-androidTest.apk` |
| Dispositivo/emulador | Ninguno conectado; no se detectaron AVD ni imÃ¡genes de sistema instaladas | Consulta local de ADB y del SDK |

El porcentaje de cobertura incluye Activities y adapters. No se excluyen para mejorar la cifra; los tests JVM no ejercitan las pantallas ni el Keystore. El reporte no mide la ejecuciÃ³n de Espresso.

## Trazabilidad de los nuevos tests

Los casos complementarios estÃ¡n en [CASOS_ROBUSTEZ.csv](CASOS_ROBUSTEZ.csv). Se conserva la planilla local de Roni sin adjudicar sus cambios a esta revisiÃ³n.

| Historia | Caso | Test | Resultado de esta revisiÃ³n | Hallazgo |
|---|---|---|---|---|
| HUM-11 Seguridad del acceso | TC-11 | `ApiCallbackTest#respuesta401_resuelta_noMuestraOtroError` | PasÃ³ | 401 resuelto sin segundo mensaje de error |
| HUM-11 Seguridad del acceso | TC-11 | `ApiCallbackTest#respuesta401_sinRedireccion_conservaMensajeDeError` | PasÃ³ | Un cliente sin redirecciÃ³n conserva el error de autenticaciÃ³n |
| HUM-06 Usuarios | SEC-02 | `ApiCallbackTest#respuesta403_noCierraSesion` | PasÃ³ | Falta de permisos no equivale a sesiÃ³n vencida |
| HUM-12 CRUD de recursos | TC-07 | `ApiCallbackTest#respuesta204_esExitoSinCuerpo` | PasÃ³ | EliminaciÃ³n exitosa sin cuerpo JSON |
| HUM-08 Contacto | TC-21 | `ContactoFlujoTest#contactoValido_conservaConsultaEnPantallaSinEnviar` | Compilado; pendiente de ejecuciÃ³n | Antes cerraba la pantalla anunciando un envÃ­o inexistente |
| HUM-12 CRUD de recursos | TC-22 | Manual: alta con catÃ¡logo no disponible | Pendiente de ejecuciÃ³n | Antes enviaba IDs 1 aunque no hubiera selecciÃ³n real |

Los hallazgos de Contacto y catÃ¡logos todavÃ­a deben vincularse a Issues si el equipo decide registrarlos. Los 4 tests JVM verifican la polÃ­tica del callback; no sustituyen una prueba de navegaciÃ³n real ante 401.

## Pendientes antes de entregar

| Pendiente | Responsable segÃºn el traspaso/issues | QuÃ© falta verificar |
|---|---|---|
| Publicar esta revisiÃ³n y ejecutar CI | Gonzalo | PR de esta rama a `develop`, revisiÃ³n y ejecuciÃ³n remota del workflow; luego integraciÃ³n a `main` |
| Cambios locales de Jhony | Jonathan | App `116c56e`/`752bf7f`, backend `3ef16be` y Wiki `394ce22` no se encuentran en este checkout. Confirmar push/PR, integraciÃ³n y reload del backend |
| Testing local de Roni | Roni / Gonzalo | Hay diferencias locales respecto de `develop` en `docs/testing/` y `tests/api/`, y la planilla CSV estÃ¡ sin versionar. Revisarlas y publicar los archivos acordados sin perder ese trabajo |
| Postman/Newman | Roni, [#27](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/27) | Ejecutar con credenciales de prueba fuera del repositorio, guardar reporte sanitizado y resultado real; no se ejecutÃ³ en esta revisiÃ³n |
| Accesibilidad | Roni, [#28](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/28) | TalkBack en flujo completo, fuente y pantalla mÃ¡ximas, orientaciÃ³n y revisiÃ³n de las pantallas principales. Tres hallazgos ya documentados en #39 no acreditan por sÃ­ solos la participaciÃ³n individual requerida |
| Compatibilidad Android 5 | Equipo | Ejecutar en API 21/22 y comprobar almacenamiento en memoria, cierre de proceso y ausencia de crashes |
| Espresso | Gonzalo, [#40](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/40) | Reejecutar los 5 tests con AccessibilityChecks. La evidencia del 12/10 documenta 4 tests previos, no la nueva suite |
| Wiki de Testing | Roni / Gonzalo | Enlazar planilla, matriz, informe, cobertura, reportes y Actions; justificar UI Automator si no hay interacciÃ³n con apps externas/permisos |
| Wiki de Ciberseguridad | Jonathan, [#30](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/30) | Responsables y frecuencia por polÃ­tica, relaciÃ³n con requisitos, permisos por rol, logs y evidencias reales de backend |
| APK descargable / release | Gonzalo, [#31](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/31) | Publicar un APK accesible a los docentes y colocar enlace. El APK local estÃ¡ firmado para depuraciÃ³n; no se publicÃ³ una release |
| Demo grupal final | Todo el equipo, #31 | Video de hasta 3 minutos con participaciÃ³n del equipo; mostrar CRUD y autenticaciÃ³n/autorizaciÃ³n, publicar enlace y verificar permisos de acceso |
| Proyecto / Scrum | Gonzalo | IEEE830 actualizado, Planning/Daily/Review/Retro con datos reales, milestone, Kanban y aportes individuales visibles |
| Cierre en main | Gonzalo | Integrar siguiendo feature â†’ develop â†’ main; cerrar issues solo con el PR final y evidencia verificada |

La consulta a GitHub del 13/10 no mostrÃ³ PR abiertos en la app. Esta revisiÃ³n no publicÃ³ cambios en GitHub, no modificÃ³ Wiki/backend y no desplegÃ³ PythonAnywhere.

## VerificaciÃ³n manual de las correcciones

1. Contacto: completar datos vÃ¡lidos y tocar **Validar consulta**. Debe informar que no se enviÃ³ ni guardÃ³, conservar los campos y no abrir otras apps. Con correo invÃ¡lido, conservar el error por campo.
2. Recursos: sin conectividad, abrir un alta, completar tÃ­tulo/URL y guardar. No debe enviar IDs inventados. Recuperar conexiÃ³n, reintentar, revisar categorÃ­a/asignatura y guardar solo con valores reales.
3. CatÃ¡logos: simular 401 al cargar categorÃ­as o asignaturas. Debe cerrar sesiÃ³n y abrir login una sola vez.
4. Usuarios, Registro, Perfil y Recursos: simular 401 en cada operaciÃ³n protegida. Debe volver al login sin permitir volver al Ã¡rea autenticada con AtrÃ¡s. Un 403 debe conservar la sesiÃ³n.
5. Login: credenciales incorrectas (401) deben mostrar error de acceso, sin tratarlo como vencimiento de sesiÃ³n.

## Archivos locales preservados

Antes de incorporar `develop`, se respaldaron los archivos locales sin seguimiento en `app/build/respaldo-revision-sprint2/testing/` y `app/build/respaldo-revision-sprint2/tests/`. DespuÃ©s se restituyeron sus contenidos en las rutas originales. El respaldo tambiÃ©n contiene el texto descargado del ABP. Es una carpeta ignorada por Git; conservarla hasta acordar las diferencias con Roni y no ejecutar una limpieza de build mientras se necesite.

## ActualizaciÃ³n del 14/10/2026

El estado de la tabla inicial corresponde a la revisiÃ³n del 13/10. DespuÃ©s se ejecutaron Newman y la suite instrumentada en Moto g85 Android 16: 24 requests / 114 assertions (3 fallas de SLA) y 7 tests instrumentados aprobados. La planilla se consolidÃ³ en 28 casos con frecuencia, polÃ­ticas y evidencia; matriz e informe se actualizaron. Se corrigieron encabezados, regiones de anuncio y la etiqueta cortada de Login. Cobertura JVM final: 282/1049 lÃ­neas (26,9%).

Evidencias: [API](../testing/RESULTADOS_API.md), [Espresso](EVIDENCIA_ESPRESSO.md), [accesibilidad](../testing/Informe_Accesibilidad.md), [planilla](../testing/Planilla_Test_Cases.csv) y [contenido preparado para la Wiki](../testing/Testing_Wiki.md).

Siguen pendientes: escucha manual con TalkBack, Scanner del resto del equipo, pantalla mÃ¡xima, API 21, email verificado de Roni, publicaciÃ³n de Wiki y cierre de entrega en main. El recurso temporal usado en el recorrido fue eliminado. Los archivos privados y herramientas locales permanecen ignorados por Git.

## Estado de cierre — 14/10/2026

- 113 tests JVM aprobados: 92 de lógica/modelos/preferencias y 21 de red con MockWebServer.
- 21 instrumentados aprobados en Moto g85 Android 16: flujos Espresso, ATF en once pantallas y apertura/regreso del navegador con UI Automator.
- Newman: 47 requests, 208 assertions, 4 fallas de SLA; 0 fallas funcionales/contrato y 0 fixtures pendientes.
- Maquetador: 13/13 comprobaciones del servidor aprobadas, sin errores de limpieza.
- Cobertura JVM: 339/1228 líneas (27.6%), sin excluir Activities ni adapters. Espresso no se suma a esta cobertura.
- APK 0.2.0 release firmado, con R8 y reducción de recursos; clave privada fuera de Git.
- Selector Claro/Oscuro/Sistema y previsualización integrados desde el PR #48. Equipo de Quiénes somos actualizado a los cuatro activos.

Evidencias: [Espresso/ATF/sistema](../pruebas/evidencias/espresso-cierre-sprint2.html), [Newman](../../tests/api/reporte_newman.json), [maquetador](../../tests/api/reporte_maquetador.json), [matriz](Matriz_Trazabilidad.md) y [41 casos](Planilla_Test_Cases.csv).

Límites: Scanner tiene evidencia de Home y el informe previo de Jonathan; falta completar el resto de pantallas de esta versión. Se capturó audio real de TalkBack en Home; falta completar y validar el flujo Login → Home → Recursos → Detalle. API 21 no se da por aprobada: se preparó una imagen oficial, pero no se logró iniciar el emulador sin aceleración en esta PC. TC-22 sin catálogos y TC-27 listado vacío siguen pendientes en entorno controlado. La reunión de Review/Retrospective y el video requieren participación real del equipo.

La latencia queda registrada en [#53](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/53). Se conserva el umbral orientativo de 800 ms; el resultado global de Newman sigue con fallas de rendimiento.

## Estado de cierre — 14/10/2026

- 113 tests JVM aprobados: 92 de lógica/modelos/preferencias y 21 de red con MockWebServer.
- 21 instrumentados aprobados en Moto g85 Android 16: flujos Espresso, ATF en once pantallas y apertura/regreso del navegador con UI Automator.
- Newman: 47 requests, 208 assertions, 4 fallas de SLA; 0 fallas funcionales/contrato y 0 fixtures pendientes.
- Maquetador: 13/13 comprobaciones del servidor aprobadas, sin errores de limpieza.
- Cobertura JVM: 339/1228 líneas (27.6%), sin excluir Activities ni adapters. Espresso no se suma a esta cobertura.
- APK 0.2.0 release firmado, con R8 y reducción de recursos; clave privada fuera de Git.
- Selector Claro/Oscuro/Sistema y previsualización integrados desde el PR #48. Equipo de Quiénes somos actualizado a los cuatro activos.

Evidencias: [Espresso/ATF/sistema](../pruebas/evidencias/espresso-cierre-sprint2.html), [Newman](../../tests/api/reporte_newman.json), [maquetador](../../tests/api/reporte_maquetador.json), [matriz](Matriz_Trazabilidad.md) y [41 casos](Planilla_Test_Cases.csv).

Límites: Scanner tiene evidencia de Home y el informe previo de Jonathan; falta completar el resto de pantallas de esta versión. Se capturó audio real de TalkBack en Home; falta completar y validar el flujo Login → Home → Recursos → Detalle. API 21 no se da por aprobada: se preparó una imagen oficial, pero no se logró iniciar el emulador sin aceleración en esta PC. TC-22 sin catálogos y TC-27 listado vacío siguen pendientes en entorno controlado. La reunión de Review/Retrospective y el video requieren participación real del equipo.

La latencia queda registrada en [#53](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/53). Se conserva el umbral orientativo de 800 ms; el resultado global de Newman sigue con fallas de rendimiento.
