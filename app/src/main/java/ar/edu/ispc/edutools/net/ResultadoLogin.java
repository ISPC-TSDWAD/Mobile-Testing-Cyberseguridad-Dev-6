package ar.edu.ispc.edutools.net;

/** Resultado de un intento de login: salio bien o hay un mensaje de error para mostrar. */
public class ResultadoLogin {

    private final boolean exito;
    private final String mensaje;

    private ResultadoLogin(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public static ResultadoLogin ok() {
        return new ResultadoLogin(true, null);
    }

    public static ResultadoLogin error(String mensaje) {
        return new ResultadoLogin(false, mensaje);
    }

    public boolean isExito() { return exito; }

    /** Mensaje de error en espanol. Es null si el login salio bien. */
    public String getMensaje() { return mensaje; }
}
