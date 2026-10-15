# Matriz de Trazabilidad — EduTools Mobile

Relaciona cada historia de usuario con sus casos de prueba, el test automatizado (`Clase#metodo`), el resultado de la ultima ejecucion y los bugs asociados. Los casos estan en [Planilla_Test_Cases.csv](Planilla_Test_Cases.csv).

Resultados: **Paso** = ejecutado y aprobado; **Pendiente** = todavia no se ejecuto; **Manual** = sin automatizar (justificado en la planilla).

| Historia de Usuario | Caso | Test automatizado (Clase#metodo) | Resultado | Bug asociado |
|---|---|---|---|---|
| HUM-01 Pantalla de acceso | TC-01 | `LoginFlujoTest#loginVacio_marcaErrorEnUsuarioYContrasena_yNoAvanza` | Paso | — |
| HUM-01 Pantalla de acceso | TC-01 | `LoginFlujoTest#loginSoloConUsuario_marcaErrorSoloEnContrasena` | Paso | — |
| HUM-01 Pantalla de acceso | TC-11 | `JwtUtilTest#tokenConExpPasada_estaVencido`, `#tokenSinExpONulo_seTrataComoVencido`, `#tokenMalFormado_noRompeYNoEsAdmin` | Paso | — |
| HUM-01 Pantalla de acceso | TC-11 | `SessionManagerTest#tokenVencido_noHaySesionValida` | Paso | — |
| HUM-01 Pantalla de acceso | TC-14 | Manual (smoke) | Paso | — |
| HUM-02 Menu principal | TC-13 | `SessionManagerTest#cerrarSesion_borraTokensYUsuario` | Paso | — |
| HUM-05 Catalogo de recursos | TC-06 | `RecursoApiTest#listar_respondeOkYDeseriazaRecursos` | Paso | — |
| HUM-06 Listado de usuarios | TC-16 | `UsuarioApiTest#listar_leeLosUsuariosYEnviaElToken` | Paso | — |
| HUM-06 Listado de usuarios | SEC-02 | `UsuarioApiTest#listar403_loDevuelveComoErrorSinCuerpoDeUsuarios` | Paso | — |
| HUM-08 Contacto | TC-02 | `ContactoFlujoTest#contactoConEmailMalFormado_marcaErrorSoloEnCorreo` | Paso | — |
| HUM-09 Accesibilidad | TC-18 | Manual (Accessibility Scanner) | Paso (3 hallazgos corregidos, Issue #39) | #39 |
| HUM-09 Accesibilidad | TC-19 | Manual (TalkBack) | Pendiente | — |
| HUM-09 Accesibilidad | TC-20 | Manual (fuente al maximo) | Pendiente | — |
| HUM-10 Compatibilidad | TC-17 | Manual (emulador API 21) | Pendiente | — |
| HUM-11 Seguridad del acceso | TC-03 | `AuthRepositoryTest#login200_guardaLosTokens` | Paso | — |
| HUM-11 Seguridad del acceso | TC-04 | `AuthRepositoryTest#login401_muestraErrorYNoGuardaNada` | Paso | — |
| HUM-11 Seguridad del acceso | TC-15 | `AuthRepositoryTest#login500_muestraErrorDelServidor`, `#sinConexion_muestraSinConexion` | Paso | — |
| HUM-11 Seguridad del acceso | TC-09 | `RegistroFlujoTest#registroConContrasenasDistintas_marcaErrorEnLaRepeticion` | Paso | — |
| HUM-11 Seguridad del acceso | TC-10 | `ValidadorPasswordTest` (8 tests) | Paso | — |
| HUM-11 Seguridad del acceso | TC-12 | Manual (celular fisico) | Paso | — |
| HUM-11 Seguridad del acceso | TC-08 | Postman `REC-08` y `USR-03` | Paso (contrato; 13/10) | — |
| HUM-11 Seguridad del acceso | SEC-01 | `AuthInterceptorTest#conToken_enviaHeaderAuthorizationBearer`, `#sinToken_noEnviaHeaderAuthorization`, `#despuesDeCerrarSesion_noEnviaHeaderAuthorization` | Paso | — |
| HUM-11 Seguridad del acceso | SEC-04 | Postman `USR-04` | Paso (contrato; 13/10) | — |
| HUM-12 CRUD de recursos | TC-05 | `RecursoApiTest#crear_hacePostConCamposValidosYSinCreadoPor` | Paso | — |
| HUM-12 CRUD de recursos | TC-07 | Postman, carpeta Recursos (`REC-03` a `REC-13`) | Paso (contrato; 13/10) | — |
| HUM-12 CRUD de recursos | SEC-03 | Postman `REC-12` y `REC-13` | Paso (contrato; 13/10) | — |

## Cobertura de la API en Postman

| Carpeta | Requests | Casos negativos |
|---|---|---|
| Auth | 6 | AUTH-02 (401), AUTH-03 (400), AUTH-05 (401) |
| Usuarios | 5 | USR-03 (401), USR-04 (401) |
| Recursos | 13 | REC-07 (400), REC-08 (401), REC-09 (404), REC-11 (404) |

## Totales de tests automaticos

La suite local (`./gradlew test`) y los 3 flujos Espresso estan detallados en [PRUEBAS_UNITARIAS.md](../pruebas/PRUEBAS_UNITARIAS.md) y [EVIDENCIA_ESPRESSO.md](../pruebas/EVIDENCIA_ESPRESSO.md). Resultados de la corrida local del 13/10/2026. Newman conserva las fallas del SLA de 800 ms.

| Capa | Total | Fallas |
|---|---|---|
| Unitaria local y red (JUnit) | 87 | 0 |
| UI instrumentada (Espresso) | 7 tests: 3 flujos y estructura | 0 (14/10, Moto g85 Android 16) |
| API externa (Newman) | 24 requests / 114 assertions | 3 por SLA de login; 0 funcionales/contrato |

## Correcciones y tests de robustez

| Historia | Caso | Test | Resultado | Evidencia |
|---|---|---|---|---|
| HUM-08 Contacto | TC-21 | `ContactoFlujoTest#contactoValido_conservaConsultaEnPantallaSinEnviar` | Pasó | [Espresso](../pruebas/evidencias/espresso-13-10.html) |
| HUM-12 Recursos | TC-22 | Manual: alta sin catálogos | Pendiente | No se ejecutó aún |
| HUM-11 Sesión | TC-11 | `ApiCallbackTest#respuesta401_resuelta_noMuestraOtroError` | Pasó | [Unitarias](../pruebas/PRUEBAS_UNITARIAS.md) |
| HUM-11 Sesión | TC-11 | `ApiCallbackTest#respuesta401_sinRedireccion_conservaMensajeDeError` | Pasó | Mismo reporte |
| HUM-06 Usuarios | SEC-02 | `ApiCallbackTest#respuesta403_noCierraSesion` | Pasó | Mismo reporte |
| HUM-12 Recursos | TC-07 | `ApiCallbackTest#respuesta204_esExitoSinCuerpo` | Pasó | Mismo reporte |

La colección es el aporte automatizado de Roni; la consolidación y ejecución del 13/10 las realizó Gonzalo. Las pruebas manuales previas y Scanner de Jonathan conservan su autoría y su fecha. TalkBack, fuente máxima, API 21 y el hallazgo individual de cada integrante no se dan por aprobados sin evidencia.

Reportes: [Newman sanitizado](../../tests/api/reporte_newman.json), [resultados de API](RESULTADOS_API.md), [Espresso](../pruebas/EVIDENCIA_ESPRESSO.md) y [cobertura](../pruebas/PRUEBAS_UNITARIAS.md).

## Accesibilidad verificada el 14/10/2026

| Historia | Caso | Test | Resultado | Hallazgo |
|---|---|---|---|---|
| HUM-09 | TC-23 | `AccesibilidadEstructuraTest#home_titulosSeIdentificanComoEncabezados` | Pasó | ACC-GV-01: encabezados de sección sin semántica |
| HUM-09 | TC-24 | `AccesibilidadEstructuraTest#resultadosDeListados_tienenAnuncioPolite` | Pasó | ACC-GV-02: resultados dinámicos sin región de anuncio |
| HUM-09 | TC-20 | Revisión visual de fuente 200% | Etiqueta Login corregida; falta tamaño de pantalla máximo y escucha de TalkBack | ACC-GV-03: etiqueta cortada |

Evidencia: [informe de accesibilidad](Informe_Accesibilidad.md) y [corrida final](../pruebas/evidencias/espresso-14-10.html).
