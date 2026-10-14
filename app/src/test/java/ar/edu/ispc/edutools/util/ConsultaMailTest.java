package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConsultaMailTest {

    @Test
    public void asunto_llevaElPrefijoDeLaApp() {
        assertEquals("[EduTools Mobile] No abre el video", ConsultaMail.asunto("  No abre el video "));
    }

    @Test
    public void cuerpo_incluyeMensajeNombreYCorreo() {
        String cuerpo = ConsultaMail.cuerpo("Ana Perez", "ana@correo.com", "Hola, tengo una duda");
        assertTrue(cuerpo.startsWith("Hola, tengo una duda"));
        assertTrue(cuerpo.contains("Ana Perez"));
        assertTrue(cuerpo.endsWith("ana@correo.com"));
    }

    @Test
    public void conNulos_noRompe() {
        assertEquals("[EduTools Mobile] ", ConsultaMail.asunto(null));
        assertEquals("\n\n--\n\n", ConsultaMail.cuerpo(null, null, null));
    }
}
