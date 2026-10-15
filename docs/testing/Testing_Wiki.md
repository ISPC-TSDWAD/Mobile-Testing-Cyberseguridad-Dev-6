# Testing — Sprint 2 / Evidencia 3

Equipo activo: Gonzalo Velasco, Jonathan Guillén, Gerardo Romero y Roni Duncan. Cierre: 14/10/2026.

## Estrategia y ejecución

113 tests JVM: 92 de lógica/modelos y 21 de red con MockWebServer. 21 instrumentados: cuatro flujos de validación, estructura y ATF en once pantallas, más el flujo de navegador con UI Automator. 47 requests Newman y 13 comprobaciones de maquetador. Los chequeos automáticos se complementan con Scanner y TalkBack; los límites manuales se mantienen explícitos.

```powershell
.\gradlew.bat testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest assembleRelease
node tests/api/ejecutar_newman.cjs
python tests/api/verificar_maquetador.py
```

JDK 17, SDK 34, Gradle Wrapper 8.1 y Node.js/Newman 6. Configurar local.properties y el entorno privado de API según su README; no publicar secretos. Para el test protegido de formulario y sistema se suministra testAccessToken solo durante la ejecución, sin exportarlo en reportes. El CI compila release sin firma si no hay archivo privado; el APK de descarga se firma localmente.

La app ahora abre enlaces en otra app: AUT-SYS-01 es obligatorio y se verifica con UI Automator y NavegadorScreen. Appium queda como deseable.

## Resultados y enlaces

- [Plan de pruebas](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Plan_de_Pruebas.md).
- [41 casos clasificados y datos de prueba](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Planilla_Test_Cases.csv).
- [Matriz completa por método y request](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Matriz_Trazabilidad.md).
- [Cierre y límites](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/CIERRE_SPRINT2.md).
- [Informe de accesibilidad](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/testing/Informe_Accesibilidad.md).
- [21 instrumentados, 0 fallas](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/docs/pruebas/evidencias/espresso-cierre-sprint2.html).
- [Newman: 4 fallas SLA, 0 funcionales](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/tests/api/reporte_newman.json).
- [Maquetador: 13/13](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/blob/main/tests/api/reporte_maquetador.json).

- [Historial CI y artifacts](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/actions).
- [Kanban real](https://github.com/orgs/ISPC-TSDWAD/projects/7).
- [Issues de accesibilidad](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues?q=label%3Aaccesibilidad).

## Pendientes identificados

Scanner completo, flujo crítico completo con TalkBack, fuente/pantalla máximas y rotación, API 21, casos TC-22/27 en entorno controlado y aportes individuales de accesibilidad que falten. SLA #53 sigue abierto. No se atribuyen a un integrante las evidencias de otra persona.
