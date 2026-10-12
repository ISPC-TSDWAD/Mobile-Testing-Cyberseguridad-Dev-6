package ar.edu.ispc.edutools.util;

import java.util.regex.Pattern;

/**
 * Validaciones de los formularios de la app.
 * Es Java puro (sin Android) para poder probarlo con JUnit.
 */
public final class Validador {

    /** Usuario que se usa cuando el campo del login queda vacio. */
    public static final String USUARIO_POR_DEFECTO = "invitado";

    /** Largo maximo del titulo, igual que el campo titulo del backend. */
    public static final int TITULO_MAX = 200;

    private static final Pattern EMAIL =
            Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    private Validador() { }

    /** Devuelve true si el texto tiene formato de correo. */
    public static boolean esEmailValido(String email) {
        if (email == null) {
            return false;
        }
        return EMAIL.matcher(email.trim()).matches();
    }

    /** Saca los espacios del usuario. Si queda vacio devuelve "invitado". */
    public static String normalizarUsuario(String usuario) {
        if (usuario == null) {
            return USUARIO_POR_DEFECTO;
        }
        String limpio = usuario.trim();
        return limpio.isEmpty() ? USUARIO_POR_DEFECTO : limpio;
    }

    /** El titulo no puede ser vacio ni pasar el maximo del backend. */
    public static boolean esTituloValido(String titulo) {
        if (titulo == null) {
            return false;
        }
        int largo = titulo.trim().length();
        return largo > 0 && largo <= TITULO_MAX;
    }

    /**
     * La app solo habla HTTPS, asi que la URL de un recurso tiene que empezar
     * con https:// y no puede tener espacios. Vacia es valida (el campo es opcional).
     */
    public static boolean esUrlValida(String url) {
        if (url == null || url.trim().isEmpty()) {
            return true;
        }
        String u = url.trim();
        return u.startsWith("https://") && u.length() > "https://".length()
                && !u.contains(" ");
    }
}
