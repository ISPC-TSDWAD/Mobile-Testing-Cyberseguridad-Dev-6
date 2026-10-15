# Matriz de trazabilidad — Sprint 2

Cada método automatizado se vincula a su historia y caso. Los IDs remiten a la planilla consolidada. Resultados del cierre del 14/10/2026.

| Código / capa | Historia | Caso | Test | Resultado |
|---|---|---|---|---|
| AUT-UNIT | HUM-12 | TC-05/06 | `RecursoTest#tipo_video_se_muestra_como_video_cta` | Pasó |
| AUT-UNIT | HUM-12 | TC-05/06 | `RecursoTest#tipo_acordeon_con_tilde` | Pasó |
| AUT-UNIT | HUM-12 | TC-05/06 | `RecursoTest#tipo_infografia_con_tilde` | Pasó |
| AUT-UNIT | HUM-12 | TC-05/06 | `RecursoTest#tipos_simples` | Pasó |
| AUT-UNIT | HUM-12 | TC-05/06 | `RecursoTest#tipo_desconocido_se_muestra_tal_cual` | Pasó |
| AUT-UNIT | HUM-12 | TC-05/06 | `RecursoTest#getters_devuelven_lo_que_se_cargo` | Pasó |
| AUT-UNIT | HUM-06 | TC-16 | `UsuarioItemTest#rol_admin_se_muestra_como_administrador` | Pasó |
| AUT-UNIT | HUM-06 | TC-16 | `UsuarioItemTest#rol_asesor_se_muestra_como_asesor_pedagogico` | Pasó |
| AUT-UNIT | HUM-06 | TC-16 | `UsuarioItemTest#rol_maquetador_se_muestra_como_maquetador` | Pasó |
| AUT-UNIT | HUM-06 | TC-16 | `UsuarioItemTest#rol_desconocido_se_muestra_tal_cual` | Pasó |
| AUT-UNIT | HUM-06 | TC-16 | `UsuarioItemTest#estado_activo_e_inactivo` | Pasó |
| AUT-UNIT | HUM-06 | TC-16 | `UsuarioItemTest#getters_devuelven_lo_que_se_cargo` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/15 | `ApiCallbackTest#respuesta401_resuelta_noMuestraOtroError` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/15 | `ApiCallbackTest#respuesta401_sinRedireccion_conservaMensajeDeError` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/15 | `ApiCallbackTest#respuesta403_noCierraSesion` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/15 | `ApiCallbackTest#respuesta204_esExitoSinCuerpo` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error400_conDetail_usaElDetalleDelServidor` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error400_conCampo_muestraCampoYMensaje` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error400_conComillasEscapadas_lasMuestraBien` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error401_conDetailEnIngles_muestraMensajeEnEspanol` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error400_sinCuerpo_mensajeGenerico` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error401_sesionVencida` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error403_sinPermiso` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error404_noEncontrado` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error5xx_errorDelServidor` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error429_avisaQueHayQueEsperar` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error409_usaElDetalleDelServidor` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#error409_sinDetalle_usaElMensajeGenerico` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#codigoDesconocido_incluyeElCodigo` | Pasó |
| AUT-UNIT | HUM-11 | TC-04/15/30 | `ApiErrorMapperTest#sinRed_mensajeSinConexion` | Pasó |
| AUT-NET | HUM-11 | SEC-01 | `AuthInterceptorTest#conToken_enviaHeaderAuthorizationBearer` | Pasó |
| AUT-NET | HUM-11 | SEC-01 | `AuthInterceptorTest#sinToken_noEnviaHeaderAuthorization` | Pasó |
| AUT-NET | HUM-11 | SEC-01 | `AuthInterceptorTest#despuesDeCerrarSesion_noEnviaHeaderAuthorization` | Pasó |
| AUT-NET | HUM-11 | TC-03/04/15 | `AuthRepositoryTest#login200_guardaLosTokens` | Pasó |
| AUT-NET | HUM-11 | TC-03/04/15 | `AuthRepositoryTest#login_hacePostAApiTokenConBarraFinalYJson` | Pasó |
| AUT-NET | HUM-11 | TC-03/04/15 | `AuthRepositoryTest#login401_muestraErrorYNoGuardaNada` | Pasó |
| AUT-NET | HUM-11 | TC-03/04/15 | `AuthRepositoryTest#login500_muestraErrorDelServidor` | Pasó |
| AUT-NET | HUM-11 | TC-03/04/15 | `AuthRepositoryTest#sinConexion_muestraSinConexion` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#listar_respondeOkYDeseriazaRecursos` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#crear_hacePostConCamposValidosYSinCreadoPor` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#error401_retornaCodigo401YMensajeSesionVencida` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#error400_retornaMensajeDeErrorDeCampo` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#error500_retornaErrorDelServidor` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#detalle_traeElContenidoHtmlParaLaVistaPrevia` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#detalle_sinContenidoNiUrl_llegaComoNulosOVacios` | Pasó |
| AUT-NET | HUM-12 | TC-05/06/35 | `RecursoApiTest#editarPlantilla_mandaLosCamposEditadosYLaCopiaVuelveConEllos` | Pasó |
| AUT-NET | HUM-06 | TC-16/SEC-02 | `UsuarioApiTest#listar_leeLosUsuariosYEnviaElToken` | Pasó |
| AUT-NET | HUM-06 | TC-16/SEC-02 | `UsuarioApiTest#listar403_loDevuelveComoErrorSinCuerpoDeUsuarios` | Pasó |
| AUT-NET | HUM-06 | TC-16/SEC-02 | `UsuarioApiTest#crear_hacePostSinMandarRolNiIsStaff` | Pasó |
| AUT-NET | HUM-06 | TC-16/SEC-02 | `UsuarioApiTest#editar_hacePatchALaRutaDelUsuarioConBarraFinal` | Pasó |
| AUT-NET | HUM-06 | TC-16/SEC-02 | `UsuarioApiTest#eliminar_haceDeleteYAceptaElSinContenido204` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/12/13 | `SessionManagerTest#sinTokens_noHaySesionValida` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/12/13 | `SessionManagerTest#tokenVigente_haySesionValida` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/12/13 | `SessionManagerTest#tokenVencido_noHaySesionValida` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/12/13 | `SessionManagerTest#guardaElUsuarioDeLaSesion` | Pasó |
| AUT-UNIT | HUM-11 | TC-11/12/13 | `SessionManagerTest#cerrarSesion_borraTokensYUsuario` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenDeAsesor_leeRolYNoEsStaff` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenDeAdmin_leeRolYEsStaff` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenDeMaquetador_noEsAdmin` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#payloadConLargosDistintos_seDecodificaBien` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenNuloOVacio_noRompeYNoEsAdmin` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenMalFormado_noRompeYNoEsAdmin` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenSinClaims_devuelveNullYFalse` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenConExpFutura_noEstaVencido` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenConExpPasada_estaVencido` | Pasó |
| AUT-UNIT | HUM-11 | TC-11 | `JwtUtilTest#tokenSinExpONulo_seTrataComoVencido` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#modoNoche_correspondeALaOpcion` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#valorDesconocidoOVacioEquivaleASegunElSistema` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#opciones_siguenElOrdenClaroOscuroSistema` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#indiceFueraDeRango_devuelveSegunElSistema` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#sinPreferenciaGuardada_empiezaSegunElSistema` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#guardarYLeer_conservaLaEleccion` | Pasó |
| AUT-UNIT | HUM-02 | TC-33 | `TemaAppTest#valorInvalidoGuardado_seLeeComoSegunElSistema` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordValida_cumple_todas_las_reglas` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordDe_8_caracteres_es_muy_corta` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordMuyCorta_pide_mas_caracteres` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordSinNumero_es_invalida` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordSinCaracterEspecial_es_invalida` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordSinLetra_es_invalida` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#passwordNullOVacia_es_invalida` | Pasó |
| AUT-UNIT | HUM-11 | TC-10 | `ValidadorPasswordTest#espacios_no_cuentan_como_caracter_especial` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#email_correcto_es_valido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#email_sin_arroba_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#email_sin_dominio_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#email_vacio_o_null_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#email_con_espacios_en_el_medio_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#usuario_vacio_pasa_a_invitado` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#usuario_se_devuelve_sin_espacios` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#titulo_normal_es_valido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#titulo_vacio_o_null_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#titulo_de_200_es_valido_y_de_201_no` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#url_https_es_valida` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#url_http_es_invalida_porque_la_app_solo_usa_https` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#url_vacia_es_valida_porque_es_opcional` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#url_con_espacios_o_sin_dominio_es_invalida` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#username_valido_y_con_simbolos_permitidos` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#username_con_espacios_vacio_o_null_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#campo_obligatorio_con_texto_es_valido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#campo_obligatorio_vacio_espacios_o_null_es_invalido` | Pasó |
| AUT-UNIT | HUM-01/08/12 | TC-01/02/05/26 | `ValidadorTest#passwords_iguales_coinciden_y_distintas_no` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#contenidoComun_seMuestra` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#sinContenidoOEnBlanco_noHayNadaParaRenderizar` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#contenidoConScriptsOMarcosOEventos_seBloquea` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#htmlLegitimo_noSeBloquea` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#soloSeAbrenEnlacesHttps` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#enlacesNoHttpsOMalFormados_noSeAbren` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#host_seMuestraEnElAvisoAntesDeAbrir` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#recursoSinContenidoNiEnlace_muestraMensajeVacio` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#conSoloContenidoOSoloEnlace_hayAlgoParaMostrar` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#tipoVideo_seDistingueParaElTextoDelBoton` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#documento_traeCspAntesDelContenidoYLosColoresDelTema` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#documentoConContenidoNulo_noRompe` | Pasó |
| AUT-UNIT | HUM-12 | TC-34 | `VistaPreviaRecursoTest#colorCss_ignoraElCanalAlfa` | Pasó |
| ACC-02 | HUM-09 | TC-23/24 | `AccesibilidadEstructuraTest#home_titulosSeIdentificanComoEncabezados` | Pasó |
| ACC-02 | HUM-09 | TC-23/24 | `AccesibilidadEstructuraTest#resultadosDeListados_tienenAnuncioPolite` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#login_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#home_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#recursos_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#formulario_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#usuarios_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#perfil_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#registro_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#multimedia_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#contacto_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#acerca_sinErroresATF` | Pasó |
| ACC-02 | HUM-09 | TC-31 | `AccesibilidadPantallasTest#detalle_sinErroresATF` | Pasó |
| AUT-UI | HUM-08 | TC-02/21/25 | `ContactoFlujoTest#contactoConEmailMalFormado_marcaErrorSoloEnCorreo` | Pasó |
| AUT-UI | HUM-08 | TC-02/21/25 | `ContactoFlujoTest#contactoValido_conservaConsultaEnPantallaSinEnviar` | Pasó |
| AUT-UI | HUM-08 | TC-02/21/25 | `ContactoFlujoTest#contactoVacio_marcaLosCuatroCampos` | Pasó |
| AUT-UI | HUM-01 | TC-01 | `LoginFlujoTest#loginVacio_marcaErrorEnUsuarioYContrasena_yNoAvanza` | Pasó |
| AUT-UI | HUM-01 | TC-01 | `LoginFlujoTest#loginSoloConUsuario_marcaErrorSoloEnContrasena` | Pasó |
| AUT-UI | HUM-12 | TC-26 | `RecursoFormFlujoTest#tituloVacio_marcaErrorYConservaFormulario` | Pasó |
| AUT-UI | HUM-11 | TC-09 | `RegistroFlujoTest#registroConContrasenasDistintas_marcaErrorEnLaRepeticion` | Pasó |
| AUT-SYS-01 | HUM-12 | TC-37 | `SistemaEnlaceTest#enlaceHttps_confirmaAbreNavegadorYRegresa` | Pasó |

## API externa

| Código | Historia | Caso | Request | Resultado |
|---|---|---|---|---|
| AUT-API-01/02 | HUM-11 | TC-03/04/08 | AUTH-01 Login valido (200) | Contrato pasó; SLA falló (#53) |
| AUT-API-01/02 | HUM-11 | TC-03/04/08 | AUTH-06 Login asesor (200) | Contrato pasó; SLA falló (#53) |
| AUT-API-01/02 | HUM-11 | TC-03/04/08 | AUTH-02 Login con credenciales invalidas (401) | Contrato pasó; SLA falló (#53) |
| AUT-API-01/02 | HUM-11 | TC-03/04/08 | AUTH-03 Login sin campos (400) | Pasó |
| AUT-API-01/02 | HUM-11 | TC-03/04/08 | AUTH-04 Renovar access con refresh (200) | Pasó |
| AUT-API-01/02 | HUM-11 | TC-03/04/08 | AUTH-05 Refresh invalido (401) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-01 Mi usuario (200) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-02 Listar usuarios como admin (200) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-03 Listar usuarios sin token (401) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-04 Token manipulado (401) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-05 Listar usuarios como asesor (403) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-06 Crear usuario temporal (201) | Contrato pasó; SLA falló (#53) |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-07 Leer usuario temporal (200) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-08 Editar usuario temporal (200) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-09 Asesor no lee perfil ajeno (403) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-10 Alta con password dÃ©bil (400) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-11 Alta sin token (401) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-12 EdiciÃ³n sin token (401) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-13 Baja sin token (401) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-16 EdiciÃ³n con correo invÃ¡lido (400) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-17 Asesor no modifica perfil ajeno (403) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-18 Asesor no elimina perfil ajeno (403) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-14 Eliminar usuario temporal (204) | Pasó |
| AUT-API-01/02 | HUM-06 | TC-28/SEC-02 | USR-15 Usuario eliminado no existe (404) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-01 Listar categorias (200) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-02 Listar asignaturas (200) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-03 Crear recurso (201) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-12 Asesor no puede eliminar plantilla base (403) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-13 Plantilla sigue existiendo tras el 403 (200) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-04 Obtener el recurso creado (200) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-05 Listar recursos (200) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-06 Editar el recurso (200) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-07 Crear recurso con datos invalidos (400) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-08 Listar recursos sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-09 Recurso inexistente (404) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-14 CategorÃ­as sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-15 Asignaturas sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-16 Alta sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-17 PUT sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-18 DELETE sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-19 Detalle sin token (401) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-20 PUT con tÃ­tulo vacÃ­o (400) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-21 Asesor copia plantilla con campos editados (201) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-22 Asesor elimina su copia (204) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-23 Copia eliminada no existe (404) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-10 Eliminar el recurso (204) | Pasó |
| AUT-API-01/02 | HUM-12 | TC-07/29/32 | REC-11 El recurso eliminado ya no existe (404) | Pasó |

| AUT-API-ROL | HUM-11 | TC-36 | verificar_maquetador.py (13 casos) | Pasó |

## Hallazgos y manuales

- [ACC-GV-01](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/49): ver informe y evidencia; #52 requiere integración de la corrección y #53 sigue abierto por rendimiento.
- [ACC-GV-02](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/50): ver informe y evidencia; #52 requiere integración de la corrección y #53 sigue abierto por rendimiento.
- [ACC-GV-03](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/51): ver informe y evidencia; #52 requiere integración de la corrección y #53 sigue abierto por rendimiento.
- [ACC-GV-04](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/52): ver informe y evidencia; #52 requiere integración de la corrección y #53 sigue abierto por rendimiento.
- [SLA-01](https://github.com/ISPC-TSDWAD/Mobile-Testing-Cyberseguridad-Dev-6/issues/53): ver informe y evidencia; #52 requiere integración de la corrección y #53 sigue abierto por rendimiento.

TC-17, TC-19, TC-20, TC-22 y TC-27 conservan sus límites o pendientes: no se convierten en aprobados por disponer de código. Ver [informe](Informe_Accesibilidad.md) y [cierre](CIERRE_SPRINT2.md).
