# Evidencia de ejecuciÃ³n: tests instrumentados (Espresso)

**Fecha:** 12/10/2026 Â· **Dispositivo:** Motorola moto g85 5G, Android 16 Â· **EjecutÃ³:** Gonzalo Velasco

**Resultado:** `OK (4 tests)` en 62,7 s. Chequeos del Accessibility Test Framework activos en los tres flujos: **0 errores**.

| ID | Flujo crÃ­tico | Test (Clase#mÃ©todo) | Issue | Resultado |
|---|---|---|---|---|
| AUT-UI-01 | Acceso con campos vacÃ­os | `LoginFlujoTest#loginVacio_marcaErrorEnUsuarioYContrasena_yNoAvanza` | #24 | PasÃ³ |
| AUT-UI-01 | Acceso solo con usuario | `LoginFlujoTest#loginSoloConUsuario_marcaErrorSoloEnContrasena` | #24 | PasÃ³ |
| AUT-UI-02 | Contacto con correo mal formado | `ContactoFlujoTest#contactoConEmailMalFormado_marcaErrorSoloEnCorreo` | #24 | PasÃ³ |
| AUT-UI-03 | Registro con contraseÃ±as distintas | `RegistroFlujoTest#registroConContrasenasDistintas_marcaErrorEnLaRepeticion` | #23, #24 | PasÃ³ |

## Estructura

- Tests en `app/src/androidTest/java/ar/edu/ispc/edutools/`, con patrÃ³n AAA (Arrange, Act, Assert).
- Screen Objects en `.../screens/` (`LoginScreen`, `ContactoScreen`, `RegistroScreen`): los tests no conocen los ids de las vistas.
- `AccessibilityChecks.enable()` en cada clase: cada acciÃ³n de Espresso audita tambiÃ©n la accesibilidad de la pantalla.

## CÃ³mo ejecutarlos

Con el dispositivo conectado, desbloqueado y con las animaciones desactivadas (Opciones de desarrollador):

```
.\gradlew.bat installDebug installDebugAndroidTest
adb shell am instrument -w -r ar.edu.ispc.edutools.test/androidx.test.runner.AndroidJUnitRunner
```

`gradlew connectedAndroidTest` no se usa porque la plataforma de tests del plugin de Android 8.1.4 falla antes de instalar la app (`IllegalAccessError` de protobuf en el host). Los tests nunca llegan al dispositivo. Se registra como limitaciÃ³n del entorno.

## ReejecuciÃ³n del 13/10/2026

EjecutÃ³ Gonzalo Velasco en Motorola moto g85 5G, Android 16. **OK (5 tests), 22,526 s**, con AccessibilityChecks activo y sin suprimir chequeos. Incluye el nuevo `ContactoFlujoTest#contactoValido_conservaConsultaEnPantallaSinEnviar` (HUM-08, TC-21).

Evidencia: [salida de instrumentaciÃ³n](evidencias/espresso-13-10.txt), [reporte HTML](evidencias/espresso-13-10.html) y [JUnit XML](evidencias/espresso-13-10.xml).

Se detectaron fallos de entorno previos: Activities sin estado RESUMED y pÃ©rdida de foco. El usuario confirmÃ³ la apariciÃ³n de la ventana de contraseÃ±as guardadas. Se repitiÃ³ con pantalla encendida, animaciones desactivadas y autocompletado desactivado temporalmente. Todos esos valores se restauraron al finalizar; no se cambiÃ³ el cÃ³digo para ocultar los fallos.

Salidas previas: [primer intento](evidencias/espresso-fallo-entorno-13-10.txt), [fallo de autocompletado](evidencias/espresso-fallo-autocompletado-13-10.txt). Son fallos de ejecuciÃ³n detectados y resueltos en la preparaciÃ³n del dispositivo, no hallazgos de accesibilidad de la app.

## VerificaciÃ³n final del 14/10/2026

**OK (7 tests), 23,533 s**, en el mismo Moto g85 con Android 16. Los 5 tests anteriores volvieron a pasar y se agregaron:

- `AccesibilidadEstructuraTest#home_titulosSeIdentificanComoEncabezados` (HUM-09, TC-23).
- `AccesibilidadEstructuraTest#resultadosDeListados_tienenAnuncioPolite` (HUM-09, TC-24).

Evidencia final: [salida](evidencias/espresso-14-10.txt), [HTML](evidencias/espresso-14-10.html) y [XML](evidencias/espresso-14-10.xml). Se mantuvieron AccessibilityChecks y los ajustes temporales se restauraron. Los tests de estructura comprueban metadatos accesibles; no certifican la escucha de la voz de TalkBack.

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
