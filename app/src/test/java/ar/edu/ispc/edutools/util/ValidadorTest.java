package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ValidadorTest {

    // ---------- email ----------

    @Test
    public void email_correcto_es_valido() {
        assertTrue(Validador.esEmailValido("jonathan@ispc.edu.ar"));
        assertTrue(Validador.esEmailValido("  ana.perez+tp@gmail.com  "));
    }

    @Test
    public void email_sin_arroba_es_invalido() {
        assertFalse(Validador.esEmailValido("jonathan.ispc.edu.ar"));
    }

    @Test
    public void email_sin_dominio_es_invalido() {
        assertFalse(Validador.esEmailValido("jonathan@"));
        assertFalse(Validador.esEmailValido("jonathan@ispc"));
    }

    @Test
    public void email_vacio_o_null_es_invalido() {
        assertFalse(Validador.esEmailValido(""));
        assertFalse(Validador.esEmailValido("   "));
        assertFalse(Validador.esEmailValido(null));
    }

    @Test
    public void email_con_espacios_en_el_medio_es_invalido() {
        assertFalse(Validador.esEmailValido("jon athan@ispc.edu.ar"));
    }

    // ---------- usuario del login ----------

    @Test
    public void usuario_vacio_pasa_a_invitado() {
        assertEquals("invitado", Validador.normalizarUsuario(""));
        assertEquals("invitado", Validador.normalizarUsuario("    "));
        assertEquals("invitado", Validador.normalizarUsuario(null));
    }

    @Test
    public void usuario_se_devuelve_sin_espacios() {
        assertEquals("jguillen", Validador.normalizarUsuario("  jguillen "));
    }

    // ---------- titulo del recurso ----------

    @Test
    public void titulo_normal_es_valido() {
        assertTrue(Validador.esTituloValido("Introduccion a Java"));
    }

    @Test
    public void titulo_vacio_o_null_es_invalido() {
        assertFalse(Validador.esTituloValido(""));
        assertFalse(Validador.esTituloValido("   "));
        assertFalse(Validador.esTituloValido(null));
    }

    @Test
    public void titulo_de_200_es_valido_y_de_201_no() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append('a');
        }
        assertTrue(Validador.esTituloValido(sb.toString()));
        assertFalse(Validador.esTituloValido(sb.toString() + "a"));
    }

    // ---------- url del recurso ----------

    @Test
    public void url_https_es_valida() {
        assertTrue(Validador.esUrlValida("https://dev6.pythonanywhere.com/api/"));
    }

    @Test
    public void url_http_es_invalida_porque_la_app_solo_usa_https() {
        assertFalse(Validador.esUrlValida("http://dev6.pythonanywhere.com/api/"));
    }

    @Test
    public void url_vacia_es_valida_porque_es_opcional() {
        assertTrue(Validador.esUrlValida(""));
        assertTrue(Validador.esUrlValida(null));
    }

    @Test
    public void url_con_espacios_o_sin_dominio_es_invalida() {
        assertFalse(Validador.esUrlValida("https://"));
        assertFalse(Validador.esUrlValida("https://mi sitio.com"));
        assertFalse(Validador.esUrlValida("javascript:alert(1)"));
    }

    // ---------- alta de usuario ----------

    @Test
    public void username_valido_y_con_simbolos_permitidos() {
        assertTrue(Validador.esUsernameValido("gvelasco"));
        assertTrue(Validador.esUsernameValido("ana.perez_2026"));
    }

    @Test
    public void username_con_espacios_vacio_o_null_es_invalido() {
        assertFalse(Validador.esUsernameValido("gonzalo velasco"));
        assertFalse(Validador.esUsernameValido(""));
        assertFalse(Validador.esUsernameValido(null));
    }

    @Test
    public void campo_obligatorio_con_texto_es_valido() {
        assertTrue(Validador.esObligatorioCompleto("Consulta"));
    }

    @Test
    public void campo_obligatorio_vacio_espacios_o_null_es_invalido() {
        assertFalse(Validador.esObligatorioCompleto(""));
        assertFalse(Validador.esObligatorioCompleto("   "));
        assertFalse(Validador.esObligatorioCompleto(null));
    }

    @Test
    public void passwords_iguales_coinciden_y_distintas_no() {
        assertTrue(Validador.coincidenPasswords("Clave2026", "Clave2026"));
        assertFalse(Validador.coincidenPasswords("Clave2026", "clave2026"));
        assertFalse(Validador.coincidenPasswords(null, null));
    }
}
