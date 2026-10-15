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

    // ---------- Formularios de Acceso, Contacto y Registro (Issues #23 y #24) ----------

    /** Mismas reglas que Django: letras, numeros y @ . + - _ (hasta 150). */
    private static final Pattern USERNAME = Pattern.compile("^[A-Za-z0-9@.+_-]{1,150}$");

    /** Campo obligatorio: no puede ser null, vacio ni solo espacios. */
    public static boolean esObligatorioCompleto(String texto) {
        return texto != null && !texto.trim().isEmpty();
    }

    /** El nombre de usuario no puede tener espacios ni simbolos raros. */
    public static boolean esUsernameValido(String username) {
        if (username == null) {
            return false;
        }
        return USERNAME.matcher(username.trim()).matches();
    }

    // ---------- Contrasena robusta (pide el ABP) ----------

    /** La contrasena tiene que tener mas de 8 caracteres. */
    public static final int PASSWORD_LARGO_MIN = 9;

    /**
     * Contrasena robusta: mas de 8 caracteres, con al menos una letra,
     * un numero y un caracter especial (algo que no sea letra, numero ni espacio).
     */
    public static boolean esPasswordFuerte(String password) {
        return mensajePassword(password) == null;
    }

    /** Dice en espanol que le falta a la contrasena. Devuelve null si esta bien. */
    public static String mensajePassword(String password) {
        if (password == null || password.length() < PASSWORD_LARGO_MIN) {
            return "La contraseña tiene que tener más de 8 caracteres.";
        }
        boolean hayLetra = false;
        boolean hayNumero = false;
        boolean hayEspecial = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);
            if (Character.isLetter(c)) {
                hayLetra = true;
            } else if (Character.isDigit(c)) {
                hayNumero = true;
            } else if (!Character.isWhitespace(c)) {
                hayEspecial = true;
            }
        }
        if (!hayLetra) {
            return "Agregá al menos una letra.";
        }
        if (!hayNumero) {
            return "Agregá al menos un número.";
        }
        if (!hayEspecial) {
            return "Agregá al menos un carácter especial (por ejemplo ! # $ %).";
        }
        return null;
    }

    /** La contrasena y su repeticion tienen que ser iguales. */
    public static boolean coincidenPasswords(String password, String repeticion) {
        return password != null && password.equals(repeticion);
    }
}
