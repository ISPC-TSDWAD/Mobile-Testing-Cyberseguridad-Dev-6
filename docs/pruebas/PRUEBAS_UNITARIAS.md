# Pruebas unitarias de la app (JUnit)

Son pruebas de lÃ³gica pura, sin emulador ni dispositivo. Corren en la JVM con JUnit 4.13.2.

## CÃ³mo correrlas

Desde Android Studio: click derecho sobre `app/src/test` > *Run Tests*.

Por consola (desde la raÃ­z del repo):

```
./gradlew testDebugUnitTest
```

El reporte queda en `app/build/reports/tests/testDebugUnitTest/index.html`.

## QuÃ© se prueba

| Clase | QuÃ© se verifica | Tests |
|---|---|---|
| `Validador` (`util/`) | email, usuario del login, tÃ­tulo y URL del recurso | 19 |
| `Validador` â€” contraseÃ±a (`ValidadorPasswordTest`) | mÃ¡s de 8 caracteres, letra, nÃºmero y carÃ¡cter especial, con el mensaje de lo que falta | 8 |
| `UsuarioItem` (`model/`) | etiquetas legibles de rol y estado, getters | 6 |
| `Recurso` (`model/`) | etiquetas legibles del tipo de recurso, getters | 6 |
| `JwtUtil` (`util/`) | lee `rol` e `is_staff` del payload del token y su vencimiento (`exp`) | 10 |
| `SessionManager` (`security/`) | la sesiÃ³n es vÃ¡lida solo con token sin vencer; cerrar sesiÃ³n borra tokens y usuario | 5 |
| `AuthRepository` (`net/`) | login contra MockWebServer: 200 guarda los tokens, 401/500 y sin red muestran error | 5 |
| `UsuarioApi` (`net/`) | CRUD de usuarios contra MockWebServer: rutas, token, 403, 201 y 204 | 5 |
| `ApiErrorMapper` (`net/`) | mensajes de errores HTTP y cuerpos de error, incluidos el 429 del lÃ­mite de intentos y el 409 de dato en uso | 14 |
| `AuthInterceptor` (`net/`) | agrega Bearer y deja de enviarlo al cerrar sesiÃ³n | 3 |
| `RecursoApi` (`net/`) | lista, alta, parseo, 401, 400 y 500 con MockWebServer; el detalle trae `contenido` y `url`; al editar una plantilla el PUT manda los campos del formulario (sin `contenido`) y la copia que devuelve el servidor los conserva sin tocar la plantilla | 8 |
| `ApiCallback` (`net/`) | 401 resuelto o sin redirecciÃ³n, 403 conserva sesiÃ³n, 204 sin cuerpo | 4 |
| `TemaApp` (`util/`) | selector de tema: Claro / Oscuro / SegÃºn el sistema, modo de AppCompat, orden de las opciones, valores invÃ¡lidos y persistencia | 7 |
| `VistaPreviaRecurso` (`util/`) | vista previa del recurso: quÃ© se muestra y quÃ© se bloquea (scripts, marcos, eventos, `javascript:`), solo enlaces HTTPS, recurso sin contenido ni enlace, documento con CSP y colores del tema | 13 |

Total: **113 tests, 0 fallas y 0 errores** (`testDebugUnitTest`, 14/10/2026). Incluye los 90 tests de `develop` y 23 nuevos: selector de tema (7), vista previa de recursos (13) y contrato de la API para el detalle y la copia de plantilla (3).

## Cobertura y comprobaciones automÃ¡ticas

```bash
./gradlew testDebugUnitTest jacocoTestReport lintDebug assembleDebug assembleDebugAndroidTest
```

JaCoCo genera HTML en `app/build/reports/jacoco/jacocoTestReport/html/index.html` y XML en `app/build/reports/jacoco/jacocoTestReport/jacocoTestReport.xml`.

Resultado del 14/10/2026 sobre `develop` (no se recalculÃ³ con la rama del selector de tema y la vista previa): **282 de 1049 lÃ­neas cubiertas (26,9%)** sobre el cÃ³digo Java de producciÃ³n, incluidas Activities y adapters. No se excluyen pantallas para aumentar el porcentaje. Este reporte mide Ãºnicamente los tests JVM; las pantallas y el almacenamiento cifrado requieren pruebas instrumentadas y no estÃ¡n cubiertos por esta ejecuciÃ³n. Los tests JVM siguen pasando.

El workflow [.github/workflows/android-tests.yml](../../.github/workflows/android-tests.yml) conserva tests, cobertura, lint y APK como artifacts. EstÃ¡ preparado localmente; el resultado de GitHub Actions queda pendiente hasta publicar el cambio y ejecutar el workflow.

Los casos y resultados de la correcciÃ³n estÃ¡n en [REVISION_SPRINT2.md](REVISION_SPRINT2.md).

## Validador

| MÃ©todo | Regla | Casos probados |
|---|---|---|
| `esEmailValido` | formato `algo@dominio.ext` | correcto, sin arroba, sin dominio, vacÃ­o, null, con espacios |
| `normalizarUsuario` | saca espacios; si queda vacÃ­o devuelve `invitado` | vacÃ­o, solo espacios, null, con espacios |
| `esTituloValido` | no vacÃ­o y hasta 200 caracteres (igual que el backend) | normal, vacÃ­o, null, 200 y 201 caracteres |
| `esUrlValida` | vacÃ­a vale (es opcional); si hay valor debe empezar con `https://` | https, http, vacÃ­a, null, sin dominio, con espacios, `javascript:` |

La regla de HTTPS viene de que la app tiene el trÃ¡fico sin cifrar deshabilitado, asÃ­ que una URL `http://` no se podrÃ­a abrir.

## Roles

Los Ãºnicos roles que existen en el backend son `admin`, `asesor` y `maquetador`. El rol "docente" no existe: `UsuarioItem.getRolLegible()` lo devuelve tal cual, sin etiqueta propia, y hay un test que lo deja asentado.

## Login y usuarios con la red (Issue #25)

Estas pruebas usan `MockWebServer`: un servidor falso que corre en la PC, asÃ­ que no tocan el backend real ni necesitan internet.

| Test | QuÃ© comprueba |
|---|---|
| `login200_guardaLosTokens` | la respuesta 200 deja `access` y `refresh` guardados en el `SessionManager` |
| `login_hacePostAApiTokenConBarraFinalYJson` | el pedido es `POST /api/token/` con el usuario en JSON |
| `login401_muestraErrorYNoGuardaNada` | con credenciales malas se muestra el mensaje de `ApiErrorMapper` y no se guarda ningÃºn token |
| `login500_muestraErrorDelServidor` | error del servidor, sin token guardado |
| `sinConexion_muestraSinConexion` | si el servidor no responde se muestra "Sin conexiÃ³n" |
| `listar_leeLosUsuariosYEnviaElToken` | `GET /api/users/` lee la lista y manda `Authorization: Bearer` |
| `listar403_...` | un asesor recibe 403 al listar usuarios |
| `crear_hacePostSinMandarRolNiIsStaff` | el alta no manda `rol` ni `is_staff` (el servidor los ignora, Issue #18) |
| `editar_...` / `eliminar_...` | `PATCH` y `DELETE` a `/api/users/{id}/` (con barra final) |

`JwtUtil` solo sirve para mostrar u ocultar botones (por ejemplo el "+" de Usuarios es solo para admin). La seguridad real la hace el servidor, que revisa la firma del token en cada pedido.

Los tests de `JwtUtil` arman tokens de prueba con firma falsa, porque `JwtUtil` no verifica la firma.


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
