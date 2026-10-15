package ar.edu.ispc.edutools.model;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RecursoTest {

    private Recurso recurso(String tipo) {
        return new Recurso(1, "Titulo", "Descripcion", tipo,
                10, 20, "Programacion", "Desarrollo Web", 5, "https://ispc.edu.ar");
    }

    @Test
    public void tipo_video_se_muestra_como_video_cta() {
        assertEquals("Video CTA", recurso("video").getTipoLegible());
    }

    @Test
    public void tipo_acordeon_con_tilde() {
        assertEquals("Acordeón", recurso("acordeon").getTipoLegible());
    }

    @Test
    public void tipo_infografia_con_tilde() {
        assertEquals("Infografía", recurso("infografia").getTipoLegible());
    }

    @Test
    public void tipos_simples() {
        assertEquals("Cuestionario", recurso("cuestionario").getTipoLegible());
        assertEquals("Lectura", recurso("lectura").getTipoLegible());
        assertEquals("Actividad", recurso("actividad").getTipoLegible());
    }

    @Test
    public void tipo_desconocido_se_muestra_tal_cual() {
        assertEquals("podcast", recurso("podcast").getTipoLegible());
    }

    @Test
    public void getters_devuelven_lo_que_se_cargo() {
        Recurso r = recurso("video");
        assertEquals(1, r.getId());
        assertEquals("Titulo", r.getTitulo());
        assertEquals(10, r.getCategoria());
        assertEquals(20, r.getAsignatura());
        assertEquals("Programacion", r.getCategoriaNombre());
        assertEquals("Desarrollo Web", r.getAsignaturaNombre());
        assertEquals(Integer.valueOf(5), r.getCreadoPor());
        assertEquals("https://ispc.edu.ar", r.getUrl());
    }
}
