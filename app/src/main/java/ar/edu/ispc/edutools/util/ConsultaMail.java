package ar.edu.ispc.edutools.util;

/**
 * Arma el texto del correo de la consulta. Es Java puro para poder probarlo con JUnit.
 * El envio lo hace la app de correo del telefono: EduTools no manda nada por su cuenta.
 */
public final class ConsultaMail {

    public static final String DESTINO = "soporte@edutools.edu.ar";

    private ConsultaMail() { }

    public static String asunto(String asuntoIngresado) {
        return "[EduTools Mobile] " + limpiar(asuntoIngresado);
    }

    public static String cuerpo(String nombre, String correo, String mensaje) {
        return limpiar(mensaje) + "\n\n--\n" + limpiar(nombre) + "\n" + limpiar(correo);
    }

    private static String limpiar(String texto) {
        return texto == null ? "" : texto.trim();
    }
}
