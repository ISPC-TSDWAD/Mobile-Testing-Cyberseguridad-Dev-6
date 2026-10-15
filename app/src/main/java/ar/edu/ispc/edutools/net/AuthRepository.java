package ar.edu.ispc.edutools.net;

import java.io.IOException;

import ar.edu.ispc.edutools.security.SessionManager;
import retrofit2.Response;

/**
 * Hace el login contra api/token/ y guarda los tokens con SessionManager.
 * El metodo login() es sincrono: la Activity lo llama desde un hilo aparte.
 * Nunca se escriben los tokens ni la contrasena en ningun log.
 */
public class AuthRepository {

    private final AuthApi api;
    private final SessionManager sesion;

    public AuthRepository(AuthApi api, SessionManager sesion) {
        this.api = api;
        this.sesion = sesion;
    }

    public ResultadoLogin login(String usuario, String password) {
        try {
            Response<LoginResponse> respuesta = api.login(new LoginRequest(usuario, password)).execute();
            LoginResponse cuerpo = respuesta.body();
            if (respuesta.isSuccessful() && cuerpo != null) {
                sesion.guardarSesion(cuerpo.getAccess(), cuerpo.getRefresh());
                sesion.guardarUsuario(usuario);
                return ResultadoLogin.ok();
            }
            String error = respuesta.errorBody() != null ? respuesta.errorBody().string() : null;
            return ResultadoLogin.error(ApiErrorMapper.mensaje(respuesta.code(), error));
        } catch (IOException e) {
            return ResultadoLogin.error(ApiErrorMapper.mensajeSinRed());
        }
    }
}
