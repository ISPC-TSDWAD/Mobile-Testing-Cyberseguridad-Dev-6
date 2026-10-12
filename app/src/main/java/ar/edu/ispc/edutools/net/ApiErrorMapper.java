package ar.edu.ispc.edutools.net;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Traduce errores HTTP a mensajes en espanol para mostrar al usuario.
 * Java puro (sin Android) para poder probarlo con JUnit.
 */
public final class ApiErrorMapper {

    private static final int LARGO_MAXIMO = 200;

    /** Formato de DRF: {"detail": "mensaje"} (tambien acepta message o error). */
    private static final Pattern DETALLE =
            Pattern.compile("\"(?:detail|message|error)\"\\s*:\\s*\"([^\"]+)\"");

    /** Formato de DRF para campos: {"campo": ["mensaje"]}. */
    private static final Pattern CAMPO =
            Pattern.compile("\"([A-Za-z_]+)\"\\s*:\\s*\\[\\s*\"([^\"]+)\"");

    private ApiErrorMapper() { }

    public static String mensaje(int codigoHttp, String cuerpoError) {
        switch (codigoHttp) {
            case 400:
                String detalle = extraerDetalle(cuerpoError);
                return detalle != null ? detalle : "Los datos enviados no son válidos";
            case 401:
                return "Sesión vencida o credenciales inválidas";
            case 403:
                return "No tenés permiso para esta acción";
            case 404:
                return "No se encontró el recurso";
            default:
                if (codigoHttp >= 500 && codigoHttp < 600) {
                    return "Error del servidor, probá más tarde";
                }
                return "Ocurrió un error inesperado (código " + codigoHttp + ")";
        }
    }

    public static String mensajeSinRed() {
        return "Sin conexión";
    }

    private static String extraerDetalle(String cuerpo) {
        if (cuerpo == null || cuerpo.trim().isEmpty()) {
            return null;
        }
        Matcher d = DETALLE.matcher(cuerpo);
        if (d.find()) {
            return recortar(d.group(1));
        }
        Matcher c = CAMPO.matcher(cuerpo);
        if (c.find()) {
            return recortar(c.group(1) + ": " + c.group(2));
        }
        return null;
    }

    private static String recortar(String texto) {
        return texto.length() > LARGO_MAXIMO ? texto.substring(0, LARGO_MAXIMO) : texto;
    }
}
