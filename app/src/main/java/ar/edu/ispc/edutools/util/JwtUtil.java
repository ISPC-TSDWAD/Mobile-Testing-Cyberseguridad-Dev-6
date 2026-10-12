package ar.edu.ispc.edutools.util;

import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lee el rol y is_staff que el backend guarda dentro del token JWT (Issue #19).
 * Solo se usa para mostrar u ocultar botones. La seguridad real la hace el
 * servidor, que valida la firma en cada pedido.
 * Java puro (sin Android) para poder probarlo con JUnit.
 */
public final class JwtUtil {

    private static final Pattern ROL = Pattern.compile("\"rol\"\\s*:\\s*\"([^\"]*)\"");
    private static final Pattern IS_STAFF = Pattern.compile("\"is_staff\"\\s*:\\s*(true|false)");
    private static final String ABC =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";

    private JwtUtil() { }

    /** Devuelve el rol del token (admin, asesor o maquetador) o null si no se puede leer. */
    public static String getRol(String token) {
        String payload = leerPayload(token);
        if (payload == null) {
            return null;
        }
        Matcher m = ROL.matcher(payload);
        return m.find() ? m.group(1) : null;
    }

    /** Devuelve true solo si el token dice is_staff = true. */
    public static boolean isStaff(String token) {
        String payload = leerPayload(token);
        if (payload == null) {
            return false;
        }
        Matcher m = IS_STAFF.matcher(payload);
        return m.find() && "true".equals(m.group(1));
    }

    /** Es admin si el rol es admin o si is_staff es true. */
    public static boolean esAdmin(String token) {
        return "admin".equals(getRol(token)) || isStaff(token);
    }

    /** Un JWT tiene 3 partes separadas por punto; el payload es la del medio. */
    private static String leerPayload(String token) {
        if (token == null) {
            return null;
        }
        String[] partes = token.split("\\.");
        if (partes.length != 3) {
            return null;
        }
        byte[] bytes = decodificarBase64Url(partes[1]);
        return bytes == null ? null : new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * Decodifica Base64 URL a mano. No uso java.util.Base64 porque pide Android 8
     * y la app soporta desde Android 5.
     */
    private static byte[] decodificarBase64Url(String texto) {
        String limpio = texto.replace("=", "");
        int largo = limpio.length();
        if (largo % 4 == 1) {
            return null;
        }
        byte[] salida = new byte[largo * 6 / 8];
        int buffer = 0;
        int bits = 0;
        int pos = 0;
        for (int i = 0; i < largo; i++) {
            int valor = ABC.indexOf(limpio.charAt(i));
            if (valor < 0) {
                return null;
            }
            buffer = (buffer << 6) | valor;
            bits += 6;
            if (bits >= 8) {
                bits -= 8;
                salida[pos++] = (byte) ((buffer >> bits) & 0xFF);
            }
        }
        return salida;
    }
}
