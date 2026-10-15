package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.gson.Gson;

import org.junit.Test;

import ar.edu.ispc.edutools.model.Recurso;

/** Vista previa del recurso: que se muestra, que se bloquea y que enlaces se abren. */
public class VistaPreviaRecursoTest {

    private static Recurso recurso(String tipo, String contenido, String url) {
        String json = "{\"id\":1,\"titulo\":\"R\",\"descripcion\":\"d\",\"tipo\":\"" + tipo + "\","
                + "\"categoria\":1,\"asignatura\":1"
                + (contenido != null ? ",\"contenido\":" + new Gson().toJson(contenido) : "")
                + (url != null ? ",\"url\":" + new Gson().toJson(url) : "") + "}";
        return new Gson().fromJson(json, Recurso.class);
    }

    // ---- contenido HTML

    @Test
    public void contenidoComun_seMuestra() {
        Recurso r = recurso("lectura", "<h1>Hola</h1><p>Texto con <b>negrita</b></p>", null);
        assertEquals(VistaPreviaRecurso.Contenido.HTML, VistaPreviaRecurso.contenidoDe(r));
    }

    @Test
    public void sinContenidoOEnBlanco_noHayNadaParaRenderizar() {
        assertEquals(VistaPreviaRecurso.Contenido.NINGUNO,
                VistaPreviaRecurso.contenidoDe(recurso("lectura", null, null)));
        assertEquals(VistaPreviaRecurso.Contenido.NINGUNO,
                VistaPreviaRecurso.contenidoDe(recurso("lectura", "   \n ", null)));
    }

    @Test
    public void contenidoConScriptsOMarcosOEventos_seBloquea() {
        String[] peligrosos = {
                "<script>alert(1)</script>",
                "<SCRIPT src=x>",
                "< script>x</script>",
                "<iframe src=\"https://x\"></iframe>",
                "<object data=x>",
                "<embed src=x>",
                "<meta http-equiv=\"refresh\" content=\"0;url=http://x\">",
                "<form action=x><input></form>",
                "<a href=\"javascript:alert(1)\">x</a>",
                "<img src=x onerror=alert(1)>",
                "<body onload = \"x()\">",
                "<a href=\"data:text/html;base64,AAAA\">x</a>",
        };
        for (String html : peligrosos) {
            assertTrue(html, VistaPreviaRecurso.esPeligroso(html));
            assertEquals(html, VistaPreviaRecurso.Contenido.BLOQUEADO,
                    VistaPreviaRecurso.contenidoDe(recurso("lectura", html, null)));
        }
    }

    @Test
    public void htmlLegitimo_noSeBloquea() {
        String[] seguros = {
                "<p>Hola</p>",
                "<ul><li>Uno</li><li>Dos</li></ul>",
                "<img src=\"https://ispc.edu.ar/logo.png\" alt=\"logo\">",
                "<a href=\"https://ispc.edu.ar\">ISPC</a>",
                "<p>El \"onboarding\" y la conversación</p>",
                "<table><tr><td>a</td></tr></table>",
        };
        for (String html : seguros) {
            assertFalse(html, VistaPreviaRecurso.esPeligroso(html));
        }
    }

    // ---- enlaces

    @Test
    public void soloSeAbrenEnlacesHttps() {
        assertTrue(VistaPreviaRecurso.esEnlaceHttps("https://ispc.edu.ar/recurso"));
        assertTrue(VistaPreviaRecurso.esEnlaceHttps("  HTTPS://www.youtube.com/watch?v=abc  "));
    }

    @Test
    public void enlacesNoHttpsOMalFormados_noSeAbren() {
        String[] invalidos = {
                null, "", "   ", "http://ispc.edu.ar", "ftp://ispc.edu.ar",
                "javascript:alert(1)", "file:///sdcard/x.html", "content://x/y",
                "intent://scan/#Intent;scheme=zxing;end", "https://", "https:///sin-host",
                "https://con espacio.com", "https://usuario:clave@ispc.edu.ar", "ispc.edu.ar",
        };
        for (String url : invalidos) {
            assertFalse(String.valueOf(url), VistaPreviaRecurso.esEnlaceHttps(url));
        }
    }

    @Test
    public void host_seMuestraEnElAvisoAntesDeAbrir() {
        assertEquals("ispc.edu.ar", VistaPreviaRecurso.hostDe("https://ispc.edu.ar/x?y=1"));
        assertEquals("", VistaPreviaRecurso.hostDe("no es una url"));
    }

    // ---- que mostrar segun el recurso

    @Test
    public void recursoSinContenidoNiEnlace_muestraMensajeVacio() {
        assertTrue(VistaPreviaRecurso.sinNadaParaMostrar(recurso("actividad", null, null)));
        assertTrue(VistaPreviaRecurso.sinNadaParaMostrar(recurso("actividad", "  ", "  ")));
    }

    @Test
    public void conSoloContenidoOSoloEnlace_hayAlgoParaMostrar() {
        assertFalse(VistaPreviaRecurso.sinNadaParaMostrar(recurso("lectura", "<p>x</p>", null)));
        assertFalse(VistaPreviaRecurso.sinNadaParaMostrar(
                recurso("video", null, "https://www.youtube.com/watch?v=abc")));
    }

    @Test
    public void tipoVideo_seDistingueParaElTextoDelBoton() {
        assertTrue(VistaPreviaRecurso.esVideo(recurso("video", null, "https://x.com")));
        assertFalse(VistaPreviaRecurso.esVideo(recurso("lectura", null, "https://x.com")));
    }

    // ---- documento del WebView

    @Test
    public void documento_traeCspAntesDelContenidoYLosColoresDelTema() {
        String doc = VistaPreviaRecurso.documentoHtml("<p>Hola</p>", "#10151B", "#E6EAF0", "#9CC2F0");
        int csp = doc.indexOf("Content-Security-Policy");
        int contenido = doc.indexOf("<p>Hola</p>");
        assertTrue(csp > 0 && contenido > csp);
        assertTrue(doc.contains("default-src 'none'"));
        assertTrue(doc.contains("img-src https: data:"));
        assertFalse("la CSP no habilita scripts", doc.contains("script-src"));
        assertTrue(doc.contains("background:#10151B"));
        assertTrue(doc.contains("color:#E6EAF0"));
        assertTrue(doc.contains("a{color:#9CC2F0}"));
    }

    @Test
    public void documentoConContenidoNulo_noRompe() {
        assertTrue(VistaPreviaRecurso.documentoHtml(null, "#fff", "#000", "#00f")
                .contains("<body></body>"));
    }

    @Test
    public void colorCss_ignoraElCanalAlfa() {
        assertEquals("#10151B", VistaPreviaRecurso.colorCss(0xFF10151B));
        assertEquals("#FFFFFF", VistaPreviaRecurso.colorCss(0xFFFFFFFF));
        assertEquals("#000000", VistaPreviaRecurso.colorCss(0xFF000000));
    }
}
