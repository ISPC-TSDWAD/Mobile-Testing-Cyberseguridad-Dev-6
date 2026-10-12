package ar.edu.ispc.edutools.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApiErrorMapperTest {

    @Test
    public void error400_conDetail_usaElDetalleDelServidor() {
        assertEquals("Contraseña muy corta",
                ApiErrorMapper.mensaje(400, "{\"detail\":\"Contraseña muy corta\"}"));
    }

    @Test
    public void error400_conCampo_muestraCampoYMensaje() {
        assertEquals("email: Introduzca un correo válido.",
                ApiErrorMapper.mensaje(400, "{\"email\":[\"Introduzca un correo válido.\"]}"));
    }

    @Test
    public void error400_conComillasEscapadas_lasMuestraBien() {
        assertEquals("rol: \"docente\" no es una elección válida.",
                ApiErrorMapper.mensaje(400,
                        "{\"rol\":[\"\\\"docente\\\" no es una elección válida.\"]}"));
    }

    @Test
    public void error401_conDetailEnIngles_muestraMensajeEnEspanol() {
        assertEquals("Sesión vencida o credenciales inválidas",
                ApiErrorMapper.mensaje(401,
                        "{\"detail\":\"No active account found with the given credentials\"}"));
    }

    @Test
    public void error400_sinCuerpo_mensajeGenerico() {
        assertEquals("Los datos enviados no son válidos", ApiErrorMapper.mensaje(400, null));
        assertEquals("Los datos enviados no son válidos", ApiErrorMapper.mensaje(400, ""));
    }

    @Test
    public void error401_sesionVencida() {
        assertEquals("Sesión vencida o credenciales inválidas", ApiErrorMapper.mensaje(401, null));
    }

    @Test
    public void error403_sinPermiso() {
        assertEquals("No tenés permiso para esta acción", ApiErrorMapper.mensaje(403, null));
    }

    @Test
    public void error404_noEncontrado() {
        assertEquals("No se encontró el recurso", ApiErrorMapper.mensaje(404, null));
    }

    @Test
    public void error5xx_errorDelServidor() {
        assertEquals("Error del servidor, probá más tarde", ApiErrorMapper.mensaje(500, null));
        assertEquals("Error del servidor, probá más tarde", ApiErrorMapper.mensaje(503, null));
    }

    @Test
    public void codigoDesconocido_incluyeElCodigo() {
        assertTrue(ApiErrorMapper.mensaje(418, null).contains("418"));
    }

    @Test
    public void sinRed_mensajeSinConexion() {
        assertEquals("Sin conexión", ApiErrorMapper.mensajeSinRed());
    }
}
