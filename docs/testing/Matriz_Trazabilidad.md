# Matriz de Trazabilidad

Autor: Roni Duncan (Testing)

| Historia de Usuario | Caso de prueba | Test automatizado (Clase#método) | Resultado | Bug asociado |
|---|---|---|---|---|
| HU-COMPLETAR (Login) | TC-COMPLETAR | LoginFlujoTest#loginVacio_marcaErrorEnUsuarioYContrasena_yNoAvanza | Pasó | - |
| HU-COMPLETAR (Login) | TC-COMPLETAR | LoginFlujoTest#loginSoloConUsuario_marcaErrorSoloEnContrasena | Pasó | - |
| HU-COMPLETAR (Contacto) | TC-COMPLETAR | ContactoFlujoTest#contactoConEmailMalFormado_marcaErrorSoloEnCorreo | Pasó | - |
| HU-COMPLETAR (Registro) | TC-COMPLETAR | RegistroFlujoTest#registroConContrasenasDistintas_marcaErrorEnLaRepeticion | Pasó | - |
| HU-COMPLETAR (Token) | TC-COMPLETAR | AuthInterceptorTest (3 tests) | Pasó | - |
| HU-COMPLETAR (Errores API) | TC-COMPLETAR | ApiErrorMapperTest (11 tests) | Pasó | - |
| HU-COMPLETAR (Validaciones) | TC-COMPLETAR | ValidadorTest | Pasó | - |
| HU-COMPLETAR (JWT) | TC-COMPLETAR | JwtUtilTest (7 tests) | Pasó | - |
| HU-COMPLETAR (Login red) | TC-COMPLETAR | AuthRepositoryTest (5 tests) | Pasó | - |
| HU-COMPLETAR (Usuarios) | TC-COMPLETAR | UsuarioApiTest (5 tests) | Pasó | - |
| HU-COMPLETAR (API Recursos) | TC-COMPLETAR | Postman: carpeta Recursos (Newman) | COMPLETAR | - |
| HU-COMPLETAR (API Auth) | TC-COMPLETAR | Postman: carpeta Auth (Newman) | COMPLETAR | - |

Suite completa: 62 tests, 0 fallas (ver docs/pruebas/PRUEBAS_UNITARIAS.md y PR #37).
Los tests de Gerardo y Daniela se agregan cuando los suban.
