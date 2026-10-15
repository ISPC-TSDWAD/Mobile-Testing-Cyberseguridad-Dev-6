package ar.edu.ispc.edutools.security;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/** Pruebas de la sesion guardada (Issue #26): se restaura si el token no vencio y se corta al cerrar. */
public class SessionManagerTest {

    private MemoryTokenStorage storage;
    private SessionManager sesion;

    @Before
    public void preparar() {
        storage = new MemoryTokenStorage();
        sesion = new SessionManager(storage);
    }

    /** Token con la forma de un JWT y un exp en segundos. La firma es falsa. */
    private static String tokenConExp(long exp) {
        Base64.Encoder enc = Base64.getUrlEncoder().withoutPadding();
        String cabecera = enc.encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));
        String payload = enc.encodeToString(
                ("{\"rol\":\"asesor\",\"exp\":" + exp + "}").getBytes(StandardCharsets.UTF_8));
        return cabecera + "." + payload + ".firma";
    }

    @Test
    public void sinTokens_noHaySesionValida() {
        assertFalse(sesion.haySesionValida());
    }

    @Test
    public void tokenVigente_haySesionValida() {
        long manana = System.currentTimeMillis() / 1000 + 86400;
        sesion.guardarSesion(tokenConExp(manana), "refresh-falso");
        assertTrue(sesion.haySesionValida());
    }

    @Test
    public void tokenVencido_noHaySesionValida() {
        long ayer = System.currentTimeMillis() / 1000 - 86400;
        sesion.guardarSesion(tokenConExp(ayer), "refresh-falso");
        assertTrue(sesion.estaLogueado());
        assertFalse(sesion.haySesionValida());
    }

    @Test
    public void guardaElUsuarioDeLaSesion() {
        sesion.guardarUsuario("ana");
        assertEquals("ana", sesion.getUsuario());
    }

    @Test
    public void cerrarSesion_borraTokensYUsuario() {
        long manana = System.currentTimeMillis() / 1000 + 86400;
        sesion.guardarSesion(tokenConExp(manana), "refresh-falso");
        sesion.guardarUsuario("ana");

        sesion.cerrarSesion();

        assertFalse(sesion.haySesionValida());
        assertNull(sesion.getAccessToken());
        assertNull(sesion.getUsuario());
        assertNull(storage.getRefreshToken());
    }
}
