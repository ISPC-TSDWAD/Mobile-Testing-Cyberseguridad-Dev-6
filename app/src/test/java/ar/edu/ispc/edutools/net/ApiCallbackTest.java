package ar.edu.ispc.edutools.net;

import static org.junit.Assert.*;

import org.junit.Test;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;

/** HUM-11, TC-11/SEC-02: distingue una sesion vencida de permisos insuficientes. */
public class ApiCallbackTest {

    private static class Registro extends ApiCallback<String> {
        final boolean resolverSesion;
        int vencimientos;
        int errores;
        int exitos;
        String mensaje;

        Registro(boolean resolverSesion) {
            this.resolverSesion = resolverSesion;
        }

        @Override public boolean onSesionVencida() {
            vencimientos++;
            return resolverSesion;
        }

        @Override public void onExito(String cuerpo) { exitos++; }
        @Override public void onError(String mensaje) {
            errores++;
            this.mensaje = mensaje;
        }
    }

    private Response<String> error(int codigo) {
        return Response.error(codigo, ResponseBody.create(
                "{}", MediaType.get("application/json")));
    }

    @Test public void respuesta401_resuelta_noMuestraOtroError() {
        Registro callback = new Registro(true);
        callback.onResponse(null, error(401));
        assertEquals(1, callback.vencimientos);
        assertEquals(0, callback.errores);
        assertEquals(0, callback.exitos);
    }

    @Test public void respuesta401_sinRedireccion_conservaMensajeDeError() {
        Registro callback = new Registro(false);
        callback.onResponse(null, error(401));
        assertEquals(1, callback.vencimientos);
        assertEquals(1, callback.errores);
        assertEquals(ApiErrorMapper.mensaje(401, "{}"), callback.mensaje);
    }

    @Test public void respuesta403_noCierraSesion() {
        Registro callback = new Registro(true);
        callback.onResponse(null, error(403));
        assertEquals(0, callback.vencimientos);
        assertEquals(1, callback.errores);
        assertEquals(0, callback.exitos);
    }

    @Test public void respuesta204_esExitoSinCuerpo() {
        Registro callback = new Registro(true);
        callback.onResponse(null, Response.success(204, null));
        assertEquals(0, callback.vencimientos);
        assertEquals(0, callback.errores);
        assertEquals(1, callback.exitos);
    }
}
