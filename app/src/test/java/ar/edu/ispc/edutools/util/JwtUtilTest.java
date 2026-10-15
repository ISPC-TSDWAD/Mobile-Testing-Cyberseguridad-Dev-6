package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JwtUtilTest {

    /** Arma un token con la forma de un JWT. La firma es falsa: JwtUtil no la mira. */
    private static String token(String payloadJson) {
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        String cabecera = enc.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = enc.encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        return cabecera + "." + payload + ".firma";
    }

    @Test
    public void tokenDeAsesor_leeRolYNoEsStaff() {
        String t = token("{\"token_type\":\"access\",\"user_id\":2,\"is_staff\":false,\"rol\":\"asesor\"}");
        assertEquals("asesor", JwtUtil.getRol(t));
        assertFalse(JwtUtil.isStaff(t));
        assertFalse(JwtUtil.esAdmin(t));
    }

    @Test
    public void tokenDeAdmin_leeRolYEsStaff() {
        String t = token("{\"token_type\":\"access\",\"user_id\":1,\"is_staff\":true,\"rol\":\"admin\"}");
        assertEquals("admin", JwtUtil.getRol(t));
        assertTrue(JwtUtil.isStaff(t));
        assertTrue(JwtUtil.esAdmin(t));
    }

    @Test
    public void tokenDeMaquetador_noEsAdmin() {
        String t = token("{\"is_staff\":false,\"rol\":\"maquetador\"}");
        assertEquals("maquetador", JwtUtil.getRol(t));
        assertFalse(JwtUtil.esAdmin(t));
    }

    @Test
    public void payloadConLargosDistintos_seDecodificaBien() {
        // Prueba los tres casos de relleno de Base64 (sin "=", con 1 y con 2 faltantes).
        assertEquals("a", JwtUtil.getRol(token("{\"rol\":\"a\"}")));
        assertEquals("ab", JwtUtil.getRol(token("{\"rol\":\"ab\"}")));
        assertEquals("abc", JwtUtil.getRol(token("{\"rol\":\"abc\"}")));
    }

    @Test
    public void tokenNuloOVacio_noRompeYNoEsAdmin() {
        assertNull(JwtUtil.getRol(null));
        assertNull(JwtUtil.getRol(""));
        assertFalse(JwtUtil.isStaff(null));
        assertFalse(JwtUtil.esAdmin(""));
    }

    @Test
    public void tokenMalFormado_noRompeYNoEsAdmin() {
        assertNull(JwtUtil.getRol("esto-no-es-un-jwt"));
        assertNull(JwtUtil.getRol("a.b"));
        assertNull(JwtUtil.getRol("a.***.c"));
        assertFalse(JwtUtil.esAdmin("a.b.c"));
    }

    @Test
    public void tokenSinClaims_devuelveNullYFalse() {
        String t = token("{\"token_type\":\"access\",\"user_id\":3}");
        assertNull(JwtUtil.getRol(t));
        assertFalse(JwtUtil.isStaff(t));
    }

    // ---------- vencimiento (claim exp) ----------

    @Test
    public void tokenConExpFutura_noEstaVencido() {
        String t = token("{\"user_id\":2,\"rol\":\"asesor\",\"exp\":2000}");
        assertFalse(JwtUtil.estaVencido(t, 1999));
    }

    @Test
    public void tokenConExpPasada_estaVencido() {
        String t = token("{\"user_id\":2,\"rol\":\"asesor\",\"exp\":2000}");
        assertTrue(JwtUtil.estaVencido(t, 2000));
        assertTrue(JwtUtil.estaVencido(t, 5000));
    }

    @Test
    public void tokenSinExpONulo_seTrataComoVencido() {
        assertTrue(JwtUtil.estaVencido(token("{\"rol\":\"asesor\"}"), 1));
        assertTrue(JwtUtil.estaVencido(null, 1));
        assertTrue(JwtUtil.estaVencido("no-es-un-jwt", 1));
    }
}
