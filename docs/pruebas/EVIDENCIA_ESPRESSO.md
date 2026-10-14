# Evidencia de ejecución: tests instrumentados (Espresso)

**Fecha:** 12/10/2026 · **Dispositivo:** Motorola moto g85 5G, Android 16 · **Ejecutó:** Gonzalo Velasco

**Resultado:** `OK (4 tests)` en 62,7 s. Chequeos del Accessibility Test Framework activos en los tres flujos: **0 errores**.

| ID | Flujo crítico | Test (Clase#método) | Issue | Resultado |
|---|---|---|---|---|
| AUT-UI-01 | Acceso con campos vacíos | `LoginFlujoTest#loginVacio_marcaErrorEnUsuarioYContrasena_yNoAvanza` | #24 | Pasó |
| AUT-UI-01 | Acceso solo con usuario | `LoginFlujoTest#loginSoloConUsuario_marcaErrorSoloEnContrasena` | #24 | Pasó |
| AUT-UI-02 | Contacto con correo mal formado | `ContactoFlujoTest#contactoConEmailMalFormado_marcaErrorSoloEnCorreo` | #24 | Pasó |
| AUT-UI-03 | Registro con contraseñas distintas | `RegistroFlujoTest#registroConContrasenasDistintas_marcaErrorEnLaRepeticion` | #23, #24 | Pasó |

## Estructura

- Tests en `app/src/androidTest/java/ar/edu/ispc/edutools/`, con patrón AAA (Arrange, Act, Assert).
- Screen Objects en `.../screens/` (`LoginScreen`, `ContactoScreen`, `RegistroScreen`): los tests no conocen los ids de las vistas.
- `AccessibilityChecks.enable()` en cada clase: cada acción de Espresso audita también la accesibilidad de la pantalla.

## Cómo ejecutarlos

Con el dispositivo conectado, desbloqueado y con las animaciones desactivadas (Opciones de desarrollador):

```
.\gradlew.bat installDebug installDebugAndroidTest
adb shell am instrument -w -r ar.edu.ispc.edutools.test/androidx.test.runner.AndroidJUnitRunner
```

`gradlew connectedAndroidTest` no se usa porque la plataforma de tests del plugin de Android 8.1.4 falla antes de instalar la app (`IllegalAccessError` de protobuf en el host). Los tests nunca llegan al dispositivo. Se registra como limitación del entorno.

## Reejecución del 13/10/2026

Ejecutó Gonzalo Velasco en Motorola moto g85 5G, Android 16. **OK (5 tests), 22,526 s**, con AccessibilityChecks activo y sin suprimir chequeos. Incluye el nuevo `ContactoFlujoTest#contactoValido_conservaConsultaEnPantallaSinEnviar` (HUM-08, TC-21).

Evidencia: [salida de instrumentación](evidencias/espresso-13-10.txt), [reporte HTML](evidencias/espresso-13-10.html) y [JUnit XML](evidencias/espresso-13-10.xml).

Se detectaron fallos de entorno previos: Activities sin estado RESUMED y pérdida de foco. El usuario confirmó la aparición de la ventana de contraseñas guardadas. Se repitió con pantalla encendida, animaciones desactivadas y autocompletado desactivado temporalmente. Todos esos valores se restauraron al finalizar; no se cambió el código para ocultar los fallos.

Salidas previas: [primer intento](evidencias/espresso-fallo-entorno-13-10.txt), [fallo de autocompletado](evidencias/espresso-fallo-autocompletado-13-10.txt). Son fallos de ejecución detectados y resueltos en la preparación del dispositivo, no hallazgos de accesibilidad de la app.

## Verificación final del 14/10/2026

**OK (7 tests), 23,533 s**, en el mismo Moto g85 con Android 16. Los 5 tests anteriores volvieron a pasar y se agregaron:

- `AccesibilidadEstructuraTest#home_titulosSeIdentificanComoEncabezados` (HUM-09, TC-23).
- `AccesibilidadEstructuraTest#resultadosDeListados_tienenAnuncioPolite` (HUM-09, TC-24).

Evidencia final: [salida](evidencias/espresso-14-10.txt), [HTML](evidencias/espresso-14-10.html) y [XML](evidencias/espresso-14-10.xml). Se mantuvieron AccessibilityChecks y los ajustes temporales se restauraron. Los tests de estructura comprueban metadatos accesibles; no certifican la escucha de la voz de TalkBack.
