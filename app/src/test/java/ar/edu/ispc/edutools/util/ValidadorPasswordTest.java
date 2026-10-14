package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/** Tests de la regla de contrasena robusta (pide el ABP). */
public class ValidadorPasswordTest {

    @Test
    public void passwordValida_cumple_todas_las_reglas() {
        assertTrue(Validador.esPasswordFuerte("Clave2026!"));
        assertNull(Validador.mensajePassword("Clave2026!"));
    }

    @Test
    public void passwordDe_8_caracteres_es_muy_corta() {
        // "mas de 8" quiere decir 9 o mas
        assertFalse(Validador.esPasswordFuerte("Clave20!"));
        assertTrue(Validador.esPasswordFuerte("Clave202!a"));
    }

    @Test
    public void passwordMuyCorta_pide_mas_caracteres() {
        assertFalse(Validador.esPasswordFuerte("Ab1!"));
        assertEquals("La contraseña tiene que tener más de 8 caracteres.",
                Validador.mensajePassword("Ab1!"));
    }

    @Test
    public void passwordSinNumero_es_invalida() {
        assertFalse(Validador.esPasswordFuerte("ClaveSegura!"));
        assertEquals("Agregá al menos un número.", Validador.mensajePassword("ClaveSegura!"));
    }

    @Test
    public void passwordSinCaracterEspecial_es_invalida() {
        assertFalse(Validador.esPasswordFuerte("Clave20262"));
        assertEquals("Agregá al menos un carácter especial (por ejemplo ! # $ %).",
                Validador.mensajePassword("Clave20262"));
    }

    @Test
    public void passwordSinLetra_es_invalida() {
        assertFalse(Validador.esPasswordFuerte("123456789!"));
        assertEquals("Agregá al menos una letra.", Validador.mensajePassword("123456789!"));
    }

    @Test
    public void passwordNullOVacia_es_invalida() {
        assertFalse(Validador.esPasswordFuerte(null));
        assertFalse(Validador.esPasswordFuerte(""));
        assertEquals("La contraseña tiene que tener más de 8 caracteres.",
                Validador.mensajePassword(null));
    }

    @Test
    public void espacios_no_cuentan_como_caracter_especial() {
        assertFalse(Validador.esPasswordFuerte("Clave 2026"));
    }
}
