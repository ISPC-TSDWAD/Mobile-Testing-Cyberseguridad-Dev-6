package ar.edu.ispc.edutools.util;

import java.net.URI;
import java.util.Locale;
import java.util.regex.Pattern;

import ar.edu.ispc.edutools.model.Recurso;

/**
 * Reglas de la vista previa de un recurso (sin dependencias de Android, para poder probarlas).
 *
 * El contenido HTML se muestra en un WebView con JavaScript y acceso a archivos apagados.
 * Ademas de eso, aca se descartan los elementos que la app no ejecuta (el servidor ya los
 * rechaza al guardar; esto es una segunda barrera) y se arma un documento con una politica
 * CSP que solo deja imagenes HTTPS o embebidas.
 */
public final class VistaPreviaRecurso {

    /** Que hay para mostrar del contenido HTML del recurso. */
    public enum Contenido { HTML, BLOQUEADO, NINGUNO }

    /** Elementos y atributos que nunca se muestran: scripts, marcos, redirecciones y eventos. */
    private static final Pattern PELIGROSO = Pattern.compile(
            "<\\s*(script|iframe|frame|frameset|object|embed|applet|meta|base|form|link)\\b"
                    + "|javascript\\s*:"
                    + "|data\\s*:\\s*text/html"
                    + "|\\son\\w+\\s*=",
            Pattern.CASE_INSENSITIVE);

    private static final String CSP =
            "default-src 'none'; img-src https: data:; style-src 'unsafe-inline'";

    private VistaPreviaRecurso() {
    }

    public static boolean tieneContenido(Recurso recurso) {
        return recurso != null && recurso.getContenido() != null
                && !recurso.getContenido().trim().isEmpty();
    }

    public static boolean tieneEnlace(Recurso recurso) {
        return recurso != null && recurso.getUrl() != null && !recurso.getUrl().trim().isEmpty();
    }

    /** True si el HTML trae algo que la app no ejecuta. */
    public static boolean esPeligroso(String html) {
        return html != null && PELIGROSO.matcher(html).find();
    }

    public static Contenido contenidoDe(Recurso recurso) {
        if (!tieneContenido(recurso)) {
            return Contenido.NINGUNO;
        }
        return esPeligroso(recurso.getContenido()) ? Contenido.BLOQUEADO : Contenido.HTML;
    }

    /** Enlace valido para abrir: HTTPS, con host y sin usuario ni espacios. */
    public static boolean esEnlaceHttps(String url) {
        if (url == null) {
            return false;
        }
        String limpio = url.trim();
        if (limpio.isEmpty() || limpio.matches(".*\\s.*")) {
            return false;
        }
        try {
            URI uri = new URI(limpio);
            return "https".equalsIgnoreCase(uri.getScheme())
                    && uri.getHost() != null && !uri.getHost().isEmpty()
                    && uri.getUserInfo() == null;
        } catch (Exception e) {
            return false;
        }
    }

    /** Dominio para mostrarlo en el aviso previo a abrir el enlace. */
    public static String hostDe(String url) {
        try {
            String host = new URI(url.trim()).getHost();
            return host != null ? host : "";
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean esVideo(Recurso recurso) {
        return recurso != null && "video".equals(recurso.getTipo());
    }

    /** True si no hay nada para mostrar: ni contenido ni enlace. */
    public static boolean sinNadaParaMostrar(Recurso recurso) {
        return !tieneContenido(recurso) && !tieneEnlace(recurso);
    }

    /** Color de la app como #RRGGBB para el CSS del documento. */
    public static String colorCss(int argb) {
        return String.format(Locale.ROOT, "#%06X", argb & 0xFFFFFF);
    }

    /**
     * Documento completo para el WebView. La meta CSP va antes del contenido, que solo
     * puede agregar mas restricciones, nunca quitarlas.
     */
    public static String documentoHtml(String contenido, String colorFondo,
                                       String colorTexto, String colorEnlace) {
        return "<!DOCTYPE html><html><head><meta charset=\"utf-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">"
                + "<meta http-equiv=\"Content-Security-Policy\" content=\"" + CSP + "\">"
                + "<style>body{margin:0;padding:12px;background:" + colorFondo + ";color:" + colorTexto
                + ";font-family:sans-serif;font-size:16px;line-height:1.5;word-wrap:break-word}"
                + "a{color:" + colorEnlace + "}img{max-width:100%;height:auto}</style>"
                + "</head><body>" + (contenido != null ? contenido : "") + "</body></html>";
    }
}
