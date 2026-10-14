# Testing — EduTools Mobile

Sprint 2 / Evidencia 3. Entrega: 14/10/2026. Equipo activo: Gonzalo Velasco, Jonathan Guillén, Gerardo Romero y Roni Duncan.

Este contenido está preparado para actualizar la página Testing de la Wiki después de publicar los archivos enlazados en `main`. Mantener el contenido del Sprint 1 como historial.

## Estrategia de pruebas

La pirámide combina 72 tests JVM de lógica, modelos, sesión y callbacks; 15 tests de integración de red con MockWebServer; 7 tests instrumentados en 3 flujos críticos; y una colección Postman de 24 requests de API externa.

Las validaciones y contratos deterministas se automatizan. La escucha de TalkBack y la evaluación visual/usabilidad se revisan manualmente. UI Automator no es obligatorio para un flujo entre apps: la app no solicita permisos de cámara/ubicación/notificaciones ni abre otras apps. El permiso INTERNET es normal y no produce un diálogo en tiempo de ejecución. Appium es deseable y queda pendiente.

## Documentación y trazabilidad

- [Plan de pruebas](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Plan_de_Pruebas.md).
- [28 casos de prueba con clasificación, automatización, responsables y frecuencia](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Planilla_Test_Cases.csv).
- [Matriz de trazabilidad](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Matriz_Trazabilidad.md).
- [Informe de accesibilidad](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Informe_Accesibilidad.md).
- [Issues de calidad y accesibilidad](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues).
- [Tableros del repositorio](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/projects). El enlace directo y las columnas del tablero activo deben verificarse con el equipo antes de publicar esta página.

## Entorno y ejecución

JDK 17, Gradle Wrapper 8.1, Android SDK 34, dispositivo Android 5.0+; corrida instrumentada en Moto g85 5G con Android 16. Configurar la ruta local del SDK en `local.properties`, que está ignorado por Git. API 21/22: pendiente de ejecución.

```powershell
.\gradlew.bat testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest
.\gradlew.bat installDebug installDebugAndroidTest
adb shell am instrument -w -r ar.edu.ispc.edutools.test/androidx.test.runner.AndroidJUnitRunner
node tests/api/ejecutar_newman.cjs
```

Para Newman, preparar el entorno privado según [estas instrucciones](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/tests/api/README.md). Nunca publicar credenciales ni tokens. La preparación de Espresso y las limitaciones del entorno están en su evidencia.

## Resultados reales

| Capa | Resultado del 13/10/2026 | Evidencia |
|---|---|---|
| JVM y red simulada | 87 tests, 0 fallas | [Detalle y cobertura](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/pruebas/PRUEBAS_UNITARIAS.md) |
| UI instrumentada | 7 tests, 0 fallas (14/10), AccessibilityChecks activo | [Espresso](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/pruebas/EVIDENCIA_ESPRESSO.md) |
| API externa | 24 requests, 114 assertions, 3 fallas de SLA en login; 0 fallas funcionales/contrato | [Reporte sanitizado](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/tests/api/reporte_newman.json) |
| Cobertura JVM | 282/1049 líneas, 26,9% (14/10), incluyendo pantallas y adapters | Reporte JaCoCo generado por Gradle; no incluye ejecución de Espresso |
| Lint | 0 errores, 45 advertencias | Reporte generado por Gradle |

[GitHub Actions](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/actions) ejecuta tests JVM, cobertura, lint y compilación cuando el workflow esté integrado. Los artifacts conservan reportes y APK durante 14 días. No se afirma que haya una ejecución remota aprobada hasta verificarla.

## Accesibilidad y pendientes

La auditoría Scanner de Jonathan documentó tres hallazgos en el Issue #39, correcciones y reescaneo sin sugerencias. Se conserva su autoría. Espresso audita Login, Contacto y Registro; esos chequeos no sustituyen TalkBack ni una auditoría visual de todas las pantallas.

Quedan pendientes la escucha manual del flujo Login → Home → Recursos → Detalle, fuente/pantalla máximas en todas las pantallas, compatibilidad API 21 y los aportes individuales reales que falten. Las evidencias se actualizan en el informe; no se inventan anuncios, capturas ni hallazgos.

La colección es el aporte de automatización de Roni. El commit nuevo cc0c802 ya está asociado en GitHub a roniduncang-crypto. Los commits anteriores con emails de ejemplo siguen sin asociación; falta verificar sus aportes en main y acordar con Roni la cuenta utilizada. La consolidación y ejecución del 13/10 fueron realizadas por Gonzalo.
