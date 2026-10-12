package ar.edu.ispc.edutools.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ar.edu.ispc.edutools.security.MemoryTokenStorage;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

public class AuthInterceptorTest {

    private MockWebServer servidor;
    private MemoryTokenStorage storage;
    private OkHttpClient cliente;

    @Before
    public void preparar() throws Exception {
        servidor = new MockWebServer();
        servidor.start();
        storage = new MemoryTokenStorage();
        cliente = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(storage))
                .build();
    }

    @After
    public void limpiar() throws Exception {
        servidor.shutdown();
    }

    private RecordedRequest hacerPedido() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody("{}"));
        Request pedido = new Request.Builder().url(servidor.url("/api/prueba/")).build();
        cliente.newCall(pedido).execute().close();
        return servidor.takeRequest();
    }

    @Test
    public void conToken_enviaHeaderAuthorizationBearer() throws Exception {
        storage.saveTokens("token-de-prueba", "refresh-de-prueba");
        assertEquals("Bearer token-de-prueba", hacerPedido().getHeader("Authorization"));
    }

    @Test
    public void sinToken_noEnviaHeaderAuthorization() throws Exception {
        assertNull(hacerPedido().getHeader("Authorization"));
    }

    @Test
    public void despuesDeCerrarSesion_noEnviaHeaderAuthorization() throws Exception {
        storage.saveTokens("token-de-prueba", "refresh-de-prueba");
        storage.clear();
        assertNull(hacerPedido().getHeader("Authorization"));
    }
}
