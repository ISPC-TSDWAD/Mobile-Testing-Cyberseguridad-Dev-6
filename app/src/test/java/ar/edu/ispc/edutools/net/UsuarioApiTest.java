package ar.edu.ispc.edutools.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.edu.ispc.edutools.model.UsuarioItem;
import ar.edu.ispc.edutools.security.MemoryTokenStorage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

/** Prueba el CRUD de usuarios contra un servidor falso: rutas, token y errores. */
public class UsuarioApiTest {

    private MockWebServer servidor;
    private MemoryTokenStorage storage;
    private UsuarioApi api;

    @Before
    public void preparar() throws Exception {
        servidor = new MockWebServer();
        servidor.start();
        storage = new MemoryTokenStorage();
        storage.saveTokens("access-falso", "refresh-falso");
        api = ApiClient.create(servidor.url("/").toString(), storage, UsuarioApi.class);
    }

    @After
    public void limpiar() throws Exception {
        servidor.shutdown();
    }

    @Test
    public void listar_leeLosUsuariosYEnviaElToken() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "[{\"id\":1,\"username\":\"admin\",\"email\":\"a@x.com\",\"first_name\":\"Ana\","
                        + "\"last_name\":\"Lopez\",\"is_staff\":true,\"is_active\":true,\"rol\":\"admin\"},"
                        + "{\"id\":2,\"username\":\"ase\",\"email\":\"\",\"first_name\":\"\","
                        + "\"last_name\":\"\",\"is_staff\":false,\"is_active\":true,\"rol\":\"asesor\"}]"));

        Response<List<UsuarioDto>> respuesta = api.listar().execute();

        assertTrue(respuesta.isSuccessful());
        assertEquals(2, respuesta.body().size());
        UsuarioItem primero = respuesta.body().get(0).toItem();
        assertEquals("Ana Lopez", primero.getNombreCompleto());
        assertEquals("Administrador", primero.getRolLegible());
        // Sin nombre cargado se muestra el usuario.
        assertEquals("ase", respuesta.body().get(1).toItem().getNombreCompleto());

        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("GET", pedido.getMethod());
        assertEquals("/api/users/", pedido.getPath());
        assertEquals("Bearer access-falso", pedido.getHeader("Authorization"));
    }

    @Test
    public void listar403_loDevuelveComoErrorSinCuerpoDeUsuarios() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(403).setBody(
                "{\"detail\":\"Usted no tiene permiso para realizar esta acción.\"}"));

        Response<List<UsuarioDto>> respuesta = api.listar().execute();

        assertFalse(respuesta.isSuccessful());
        assertEquals(403, respuesta.code());
        assertNull(respuesta.body());
        assertEquals("No tenés permiso para esta acción",
                ApiErrorMapper.mensaje(respuesta.code(), null));
    }

    @Test
    public void crear_hacePostSinMandarRolNiIsStaff() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(201).setBody(
                "{\"id\":5,\"username\":\"nuevo\",\"email\":\"n@x.com\",\"first_name\":\"Nora\","
                        + "\"last_name\":\"Paz\",\"is_staff\":false,\"is_active\":true,\"rol\":\"asesor\"}"));

        Response<UsuarioDto> respuesta = api.crear(
                new NuevoUsuarioRequest("nuevo", "clave-de-prueba", "n@x.com", "Nora", "Paz")).execute();

        assertEquals(201, respuesta.code());
        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("POST", pedido.getMethod());
        assertEquals("/api/users/", pedido.getPath());
        String cuerpo = pedido.getBody().readUtf8();
        assertTrue(cuerpo.contains("\"first_name\":\"Nora\""));
        assertFalse(cuerpo.contains("rol"));
        assertFalse(cuerpo.contains("is_staff"));
    }

    @Test
    public void editar_hacePatchALaRutaDelUsuarioConBarraFinal() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(
                "{\"id\":5,\"username\":\"nuevo\",\"email\":\"z@x.com\",\"first_name\":\"Zoe\","
                        + "\"last_name\":\"Paz\",\"is_staff\":false,\"is_active\":true,\"rol\":\"asesor\"}"));

        Response<UsuarioDto> respuesta = api.editar(5, new EditarUsuarioRequest("Zoe", "Paz", "z@x.com")).execute();

        assertTrue(respuesta.isSuccessful());
        assertEquals("Zoe Paz", respuesta.body().toItem().getNombreCompleto());
        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("PATCH", pedido.getMethod());
        assertEquals("/api/users/5/", pedido.getPath());
    }

    @Test
    public void eliminar_haceDeleteYAceptaElSinContenido204() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(204));

        Response<Void> respuesta = api.eliminar(5).execute();

        assertTrue(respuesta.isSuccessful());
        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("DELETE", pedido.getMethod());
        assertEquals("/api/users/5/", pedido.getPath());
    }
}
