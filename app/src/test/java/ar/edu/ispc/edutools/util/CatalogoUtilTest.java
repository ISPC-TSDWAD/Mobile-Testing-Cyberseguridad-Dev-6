package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import ar.edu.ispc.edutools.model.ItemCatalogo;

public class CatalogoUtilTest {

    @Test
    public void conSeleccion_usaElIdElegido() {
        assertEquals(7, CatalogoUtil.resolverId(new ItemCatalogo(7, "Video"), 3));
    }

    @Test
    public void sinSeleccion_enEdicion_conservaElIdDelRecurso() {
        assertEquals(3, CatalogoUtil.resolverId(null, 3));
    }

    @Test
    public void sinSeleccion_enAlta_noInventaUnId() {
        assertEquals(CatalogoUtil.SIN_ID, CatalogoUtil.resolverId(null, 0));
    }

    @Test
    public void unObjetoQueNoEsCatalogo_seIgnora() {
        assertEquals(CatalogoUtil.SIN_ID, CatalogoUtil.resolverId("texto", -5));
    }
}
