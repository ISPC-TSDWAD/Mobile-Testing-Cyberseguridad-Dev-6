package ar.edu.ispc.edutools.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UsuarioItemTest {

    private UsuarioItem usuario(String rol, boolean activo) {
        return new UsuarioItem(1, "Ana Perez", "aperez", "ana@ispc.edu.ar", rol, activo);
    }

    @Test
    public void rol_admin_se_muestra_como_administrador() {
        assertEquals("Administrador", usuario("admin", true).getRolLegible());
    }

    @Test
    public void rol_asesor_se_muestra_como_asesor_pedagogico() {
        assertEquals("Asesor Pedagógico", usuario("asesor", true).getRolLegible());
    }

    @Test
    public void rol_maquetador_se_muestra_como_maquetador() {
        assertEquals("Maquetador", usuario("maquetador", true).getRolLegible());
    }

    @Test
    public void rol_desconocido_se_muestra_tal_cual() {
        // "docente" no es un rol del backend, asi que no tiene etiqueta propia
        assertEquals("docente", usuario("docente", true).getRolLegible());
    }

    @Test
    public void estado_activo_e_inactivo() {
        assertEquals("Activo", usuario("asesor", true).getEstadoLegible());
        assertEquals("Inactivo", usuario("asesor", false).getEstadoLegible());
    }

    @Test
    public void getters_devuelven_lo_que_se_cargo() {
        UsuarioItem u = usuario("admin", true);
        assertEquals(1, u.getId());
        assertEquals("Ana Perez", u.getNombreCompleto());
        assertEquals("aperez", u.getUsername());
        assertEquals("ana@ispc.edu.ar", u.getCorreo());
        assertTrue(u.isActivo());
    }
}
