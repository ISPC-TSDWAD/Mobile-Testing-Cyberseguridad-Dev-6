# Cierre de pruebas y entrega — EduTools Mobile

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

Verificación del APK minificado: Claro, Oscuro, Sistema y persistencia tras reinicio aprobados en dispositivo real. El recorrido de recursos se interrumpió al cambiar la ventana a Ajustes; no se registra como aprobado. [Resultados](../pruebas/evidencias/release-smoke.json).
