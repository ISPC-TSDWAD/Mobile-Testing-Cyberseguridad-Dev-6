# Plan de Pruebas — EduTools Mobile (Sprint 2)

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

Equipo activo confirmado: Gonzalo, Jonathan, Gerardo y Roni. 87 tests JVM aprobados y 5 tests Espresso aprobados con AccessibilityChecks. Newman: 24 requests, 114 assertions, 3 fallas de SLA en login; resultado global con fallas. No se dan por completos los criterios de salida. Pendientes: revisión manual de accesibilidad, compatibilidad API 21, gestión del hallazgo de SLA, atribución de commits de Roni y publicación en main/Wiki.

Actualización 14/10: 87 tests JVM y 7 instrumentados aprobados; recorrido automatizado de pantallas con fuente 200%, tres correcciones de accesibilidad y restauración de ajustes. Se mantiene pendiente la escucha de TalkBack, Scanner, tamaño de pantalla máximo y API 21. Ver informe actualizado.

GitHub, 14/10: el nuevo commit cc0c802 de Roni está vinculado a roniduncang-crypto. No se reescribe el historial anterior; queda pendiente su integración en main y verificar el requisito de aportes individuales.
