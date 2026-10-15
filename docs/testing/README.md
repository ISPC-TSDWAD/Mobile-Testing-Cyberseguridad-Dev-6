# Testing — EduTools Mobile

Indice de la documentacion de pruebas del Sprint 2.

| Documento | Contenido |
|---|---|
| [Plan_de_Pruebas.md](Plan_de_Pruebas.md) | Objetivo, alcance, enfoque, criterios, entorno, responsables y cronograma |
| [Planilla_Test_Cases.csv](Planilla_Test_Cases.csv) | 28 casos con clasificación, responsable, frecuencia, política y evidencia |
| [Matriz_Trazabilidad.md](Matriz_Trazabilidad.md) | Historia de usuario, caso, test (`Clase#metodo`), resultado y bug |
| [Informe_Accesibilidad.md](Informe_Accesibilidad.md) | Accessibility Scanner, TalkBack, checklist y bugs |
| [../../tests/api/README.md](../../tests/api/README.md) | Coleccion Postman v2.1 y ejecucion con Newman |
| [../pruebas/PRUEBAS_UNITARIAS.md](../pruebas/PRUEBAS_UNITARIAS.md) | Detalle de los tests JUnit |
| [../pruebas/EVIDENCIA_ESPRESSO.md](../pruebas/EVIDENCIA_ESPRESSO.md) | Evidencia de los flujos Espresso |
| [../pruebas/PRUEBAS_MANUALES.md](../pruebas/PRUEBAS_MANUALES.md) | Pruebas manuales en dispositivo |
| [RESULTADOS_API.md](RESULTADOS_API.md) | Ejecución real de Newman y hallazgo de SLA |

## Como ejecutar

```bash
# Tests unitarios y de red (JUnit + MockWebServer)
./gradlew testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest

# Flujos Espresso (con un celular o emulador conectado)
./gradlew installDebug installDebugAndroidTest
adb shell am instrument -w -r ar.edu.ispc.edutools.test/androidx.test.runner.AndroidJUnitRunner

# API externa (Postman + Newman), ver tests/api/README.md
node tests/api/ejecutar_newman.cjs
```

En Windows usar `.\gradlew.bat`. Para la preparación del entorno privado y la instalación de Newman ver [README de API](../../tests/api/README.md). Se usa instrumentación directa por la limitación de `connectedAndroidTest` documentada en [Espresso](../pruebas/EVIDENCIA_ESPRESSO.md).

Resultados verificados del 13/10/2026: 87 tests JVM aprobados, 7 tests instrumentados aprobados con AccessibilityChecks y 24 requests / 114 assertions de Newman. Newman conserva 3 fallas de SLA de login; no se presenta como suite completamente aprobada.

Pendientes: escucha manual con TalkBack, auditoría Scanner de las pantallas restantes y aportes individuales, Android API 21, vinculación de commits de Roni y publicación final de la documentación en main/Wiki.
