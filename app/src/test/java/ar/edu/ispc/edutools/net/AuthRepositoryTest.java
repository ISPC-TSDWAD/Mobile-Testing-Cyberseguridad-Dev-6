package ar.edu.ispc.edutools.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ispc.edutools.security.MemoryTokenStorage;
import ar.edu.ispc.edutools.security.SessionManager;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

/** Prueba del login contra un servidor falso (MockWebServer), sin tocar el backend real. */
public class AuthRepositoryTest {

    private MockWebServer servidor;
    private MemoryTokenStorage storage;
    private AuthRepository repo;

    @Before
    public void preparar() throws Exception {
        servidor = new MockWebServer();
        servidor.start();
        storage = new MemoryTokenStorage();
        AuthApi api = ApiClient.create(servidor.url("/").toString(), storage, AuthApi.class);
        repo = new AuthRepository(api, new SessionManager(storage));
    }

    @After
    public void limpiar() throws Exception {
        servidor.shutdown();
    }

    @Test
    public void login200_guardaLosTokens() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "{\"refresh\":\"refresh-falso\",\"access\":\"access-falso\","
                        + "\"is_staff\":false,\"rol\":\"asesor\"}"));

        ResultadoLogin resultado = repo.login("ana", "clave-de-prueba");

        assertTrue(resultado.isExito());
        assertNull(resultado.getMensaje());
        assertEquals("access-falso", storage.getAccessToken());
        assertEquals("refresh-falso", storage.getRefreshToken());
        assertEquals("ana", storage.getUsername());
    }

    @Test
    public void login_hacePostAApiTokenConBarraFinalYJson() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "{\"refresh\":\"r\",\"access\":\"a\",\"is_staff\":true,\"rol\":\"admin\"}"));

        repo.login("ana", "clave-de-prueba");

        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("POST", pedido.getMethod());
        assertEquals("/api/token/", pedido.getPath());
        assertTrue(pedido.getBody().readUtf8().contains("\"username\":\"ana\""));
    }

    @Test
    public void login401_muestraErrorYNoGuardaNada() {
        servidor.enqueue(new MockResponse().setResponseCode(401).setBody(
                "{\"detail\":\"No active account found with the given credentials\"}"));

        ResultadoLogin resultado = repo.login("ana", "clave-incorrecta");

        assertFalse(resultado.isExito());
        assertEquals(ApiErrorMapper.mensaje(401, null), resultado.getMensaje());
        assertNull(storage.getAccessToken());
        assertNull(storage.getRefreshToken());
    }

    @Test
    public void login500_muestraErrorDelServidor() {
        servidor.enqueue(new MockResponse().setResponseCode(500));

        ResultadoLogin resultado = repo.login("ana", "clave-de-prueba");

        assertFalse(resultado.isExito());
        assertEquals("Error del servidor, probá más tarde", resultado.getMensaje());
        assertNull(storage.getAccessToken());
    }

    @Test
    public void sinConexion_muestraSinConexion() throws Exception {
        servidor.shutdown();   // el servidor ya no responde

        ResultadoLogin resultado = repo.login("ana", "clave-de-prueba");

        assertFalse(resultado.isExito());
        assertEquals(ApiErrorMapper.mensajeSinRed(), resultado.getMensaje());
    }
}
