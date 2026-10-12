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
