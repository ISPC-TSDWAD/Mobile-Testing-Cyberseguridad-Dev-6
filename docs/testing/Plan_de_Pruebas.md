# Plan de Pruebas â€” EduTools Mobile (Sprint 2)

Formato basado en IEEE 829. Responsable: Roni Duncan (Testing). Equipo DEV6.

## 1. Objetivo

Verificar que la app EduTools Mobile cumple los criterios de aceptacion de las historias HUM-01 a HUM-12, que consume correctamente la API del backend y que es segura y accesible, dejando evidencia rastreable de cada prueba.

## 2. Alcance

**Se prueba:** login real con JWT, sesion y cierre de sesion, registro con contrasena robusta, usuarios (listado, alta, edicion, baja), recursos pedagogicos (CRUD), contacto, navegacion, accesibilidad, compatibilidad con Android 5 y la API publicada.

**No se prueba:** el backend por dentro (tiene sus propios tests en el repo `ModPWeb--Dev6`), rendimiento bajo carga ni el envio real del formulario de contacto.

## 3. Funcionalidades a probar

| Funcionalidad | Historia | Casos |
|---|---|---|
| Acceso y sesion | HUM-01, HUM-11 | TC-01, TC-03, TC-04, TC-11, TC-12, TC-13, TC-14, TC-15 |
| Registro | HUM-11 | TC-09, TC-10 |
| Usuarios | HUM-06 | TC-16, SEC-02 |
| Recursos | HUM-05, HUM-12 | TC-05, TC-06, TC-07, SEC-03 |
| Contacto | HUM-08 | TC-02 |
| Seguridad de la API | HUM-11 | TC-08, SEC-01, SEC-04 |
| Compatibilidad | HUM-10 | TC-17 |
| Accesibilidad | HUM-09 | TC-18, TC-19, TC-20 |

El detalle esta en [Planilla_Test_Cases.csv](Planilla_Test_Cases.csv) y la relacion con las historias en [Matriz_Trazabilidad.md](Matriz_Trazabilidad.md).

## 4. Enfoque

Estrategia por capas (piramide de pruebas):

| Capa | Herramienta | Que cubre |
|---|---|---|
| Unitaria local | JUnit 4 | Validaciones, JWT, sesion, mapeo de errores, modelos |
| Integracion de red | JUnit + MockWebServer | Retrofit contra respuestas simuladas (200, 400, 401, 403, 5xx, sin red) |
| UI instrumentada | Espresso con Screen Objects | 3 flujos: Login, Contacto, Registro |
| API externa | Postman v2.1 + Newman | Endpoints reales del servidor publicado |
| Sistema | Manual en celular y emulador | Sesion persistente, compatibilidad API 21, TalkBack, Accessibility Scanner |

Criterio para automatizar: ejecucion repetitiva, resultado determinista y costo de automatizar menor al de ejecutar a mano. Lo que no cumple se deja manual y se justifica en la planilla.

Tipos de prueba: funcional, negativa, regresion, smoke, integracion, seguridad, accesibilidad y compatibilidad.

## 5. Criterios de aprobacion

- Todos los tests automaticos pasan (0 fallas) con `./gradlew test`.
- Los 3 flujos Espresso pasan en un dispositivo.
- La coleccion Postman corre con Newman sin fallas contra el servidor publicado.
- No quedan casos de prioridad Alta abiertos ni bugs de seguridad sin resolver.
- Accessibility Scanner no informa sugerencias abiertas en las pantallas principales.

Una prueba falla si el resultado obtenido difiere del esperado; todo fallo se registra como Issue.

## 6. Entorno

| Elemento | Detalle |
|---|---|
| App | Android (Java), minSdk 21, targetSdk 34 |
| Dispositivos | Evidencias previas: Samsung SM-A546E. Corrida 13/10: Moto g85 5G, Android 16. API 21: pendiente, sin emulador disponible |
| API | `https://dev6.pythonanywhere.com` (HTTPS) |
| Herramientas | Android Studio, Gradle 8.1, JDK 17, Postman, Newman (Node.js), Accessibility Scanner, TalkBack |
| Usuarios de prueba | Un admin y un asesor, entregados por privado (nunca se suben al repo) |

## 7. Responsables

| Integrante | Responsabilidad de testing |
|---|---|
| Roni Duncan | Plan, planilla, coleccion Postman y Newman, informe de accesibilidad, matriz y pagina Testing de la Wiki |
| Gonzalo Velasco | Flujos Espresso (Login, Contacto, Registro) y pruebas del interceptor de token |
| Jonathan Guillen | Tests unitarios y de red de login, JWT, sesion y usuarios; auditoria de accesibilidad; seguridad |
| Gerardo Romero | Tests de red del CRUD de recursos y caso de seguridad sobre plantillas base |

Cada integrante aporta un test automatizado, un hallazgo de accesibilidad y un caso de seguridad propios.

## 8. Cronograma

| Fecha | Actividad |
|---|---|
| Hasta el 13/10 | Tests unitarios, de red y Espresso; primera auditoria de accesibilidad |
| 14/10 hasta las 14:00 | Coleccion Postman, planilla, matriz e informe de accesibilidad completos |
| 14/10 14:00 | Congelamiento del codigo; ejecucion final de toda la suite |
| 14/10 hasta las 18:00 | Pagina Testing de la Wiki y evidencias |
| 14/10 antes de las 20:00 | Pull Request final `develop` a `main` |

## 9. Riesgos

| Riesgo | Mitigacion |
|---|---|
| El servidor gratuito se apaga o tarda | Los tests de red usan MockWebServer; Postman se corre al final |
| Pocos celulares con Android 5 | Se usa un emulador API 21 |
| Datos de prueba que quedan en el servidor | La coleccion elimina lo que crea |

## 10. Estado verificado del 13/10/2026

Equipo activo confirmado: Gonzalo, Jonathan, Gerardo y Roni. 87 tests JVM aprobados y 5 tests Espresso aprobados con AccessibilityChecks. Newman: 24 requests, 114 assertions, 3 fallas de SLA en login; resultado global con fallas. No se dan por completos los criterios de salida. Pendientes: revisiÃ³n manual de accesibilidad, compatibilidad API 21, gestiÃ³n del hallazgo de SLA, atribuciÃ³n de commits de Roni y publicaciÃ³n en main/Wiki.

ActualizaciÃ³n 14/10: 87 tests JVM y 7 instrumentados aprobados; recorrido automatizado de pantallas con fuente 200%, tres correcciones de accesibilidad y restauraciÃ³n de ajustes. Se mantiene pendiente la escucha de TalkBack, Scanner, tamaÃ±o de pantalla mÃ¡ximo y API 21. Ver informe actualizado.

GitHub, 14/10: el nuevo commit cc0c802 de Roni estÃ¡ vinculado a roniduncang-crypto. No se reescribe el historial anterior; queda pendiente su integraciÃ³n en main y verificar el requisito de aportes individuales.

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
