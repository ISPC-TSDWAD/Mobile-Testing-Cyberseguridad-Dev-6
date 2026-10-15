package ar.edu.ispc.edutools.net;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.security.MemoryTokenStorage;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import retrofit2.Response;

/**
 * Pruebas unitarias para RecursoApi usando MockWebServer:
 * lista OK, crear OK, 401 (no autenticado), 400 (error de validación) y 500 (error del servidor).
 */
public class RecursoApiTest {

    private MockWebServer servidor;
    private MemoryTokenStorage storage;
    private RecursoApi api;

    @Before
    public void preparar() throws Exception {
        servidor = new MockWebServer();
        servidor.start();
        storage = new MemoryTokenStorage();
        storage.saveTokens("access-falso", "refresh-falso");
        api = ApiClient.create(servidor.url("/").toString(), storage, RecursoApi.class);
    }

    @After
    public void limpiar() throws Exception {
        servidor.shutdown();
    }

    @Test
    public void listar_respondeOkYDeseriazaRecursos() throws Exception {
        String jsonRespuesta = "[{"
                + "\"id\":1,"
                + "\"titulo\":\"Acordeón\","
                + "\"descripcion\":\"Bloque plegable\","
                + "\"tipo\":\"acordeon\","
                + "\"url\":\"https://ispc.edu.ar\","
                + "\"categoria\":2,"
                + "\"asignatura\":1,"
                + "\"categoria_nombre\":\"CTA\","
                + "\"asignatura_nombre\":\"Historia\","
                + "\"creado_por\":1"
                + "}]";

        servidor.enqueue(new MockResponse().setResponseCode(200).setBody(jsonRespuesta));

        Response<List<Recurso>> respuesta = api.listar().execute();

        assertTrue(respuesta.isSuccessful());
        assertNotNull(respuesta.body());
        assertEquals(1, respuesta.body().size());

        Recurso primero = respuesta.body().get(0);
        assertEquals(1, primero.getId());
        assertEquals("Acordeón", primero.getTitulo());
        assertEquals("CTA", primero.getCategoriaNombre());
        assertEquals("Historia", primero.getAsignaturaNombre());
        assertEquals(Integer.valueOf(1), primero.getCreadoPor());

        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("GET", pedido.getMethod());
        assertEquals("/api/pedagogia/recursos/", pedido.getPath());
        assertEquals("Bearer access-falso", pedido.getHeader("Authorization"));
    }

    @Test
    public void crear_hacePostConCamposValidosYSinCreadoPor() throws Exception {
        String jsonRespuesta = "{"
                + "\"id\":10,"
                + "\"titulo\":\"Nuevo recurso\","
                + "\"descripcion\":\"Descripcion de prueba\","
                + "\"tipo\":\"video\","
                + "\"url\":\"https://edutools.edu.ar\","
                + "\"categoria\":1,"
                + "\"asignatura\":2,"
                + "\"categoria_nombre\":\"Componentes multimedia\","
                + "\"asignatura_nombre\":\"Diseño Instruccional\","
                + "\"creado_por\":3"
                + "}";

        servidor.enqueue(new MockResponse().setResponseCode(201).setBody(jsonRespuesta));

        Recurso nuevo = new Recurso("Nuevo recurso", "Descripcion de prueba",
                "video", 1, 2, "https://edutools.edu.ar");

        Response<Recurso> respuesta = api.crear(nuevo).execute();

        assertEquals(201, respuesta.code());
        assertTrue(respuesta.isSuccessful());
        assertNotNull(respuesta.body());
        assertEquals(10, respuesta.body().getId());

        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("POST", pedido.getMethod());
        assertEquals("/api/pedagogia/recursos/", pedido.getPath());
        assertEquals("Bearer access-falso", pedido.getHeader("Authorization"));

        String cuerpoEnviado = pedido.getBody().readUtf8();
        assertTrue(cuerpoEnviado.contains("\"titulo\":\"Nuevo recurso\""));
        assertTrue(cuerpoEnviado.contains("\"tipo\":\"video\""));
        assertTrue(cuerpoEnviado.contains("\"categoria\":1"));
        assertTrue(cuerpoEnviado.contains("\"asignatura\":2"));
        // Creado por no se envia (lo asigna el servidor)
        assertFalse(cuerpoEnviado.contains("creado_por"));
    }

    @Test
    public void error401_retornaCodigo401YMensajeSesionVencida() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(401).setBody(
                "{\"detail\":\"Las credenciales de autenticación no se proveyeron.\"}"));

        Response<List<Recurso>> respuesta = api.listar().execute();

        assertFalse(respuesta.isSuccessful());
        assertEquals(401, respuesta.code());
        assertEquals("Sesión vencida o credenciales inválidas",
                ApiErrorMapper.mensaje(respuesta.code(), null));
    }

    @Test
    public void error400_retornaMensajeDeErrorDeCampo() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(400).setBody(
                "{\"titulo\":[\"Este campo es requerido.\"]}"));

        Recurso invalido = new Recurso("", "Desc", "video", 1, 1, "");
        Response<Recurso> respuesta = api.crear(invalido).execute();

        assertFalse(respuesta.isSuccessful());
        assertEquals(400, respuesta.code());
        assertNotNull(respuesta.errorBody());

        String cuerpoError = respuesta.errorBody().string();
        assertEquals("titulo: Este campo es requerido.",
                ApiErrorMapper.mensaje(respuesta.code(), cuerpoError));
    }

    @Test
    public void error500_retornaErrorDelServidor() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(500).setBody("Server Error"));

        Response<List<Recurso>> respuesta = api.listar().execute();

        assertFalse(respuesta.isSuccessful());
        assertEquals(500, respuesta.code());
        assertEquals("Error del servidor, probá más tarde",
                ApiErrorMapper.mensaje(respuesta.code(), null));
    }

    @Test
    public void detalle_traeElContenidoHtmlParaLaVistaPrevia() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody("{"
                + "\"id\":7,\"titulo\":\"Lectura\",\"descripcion\":\"Desc\",\"tipo\":\"lectura\","
                + "\"contenido\":\"<h1>Hola</h1><p>Texto</p>\",\"url\":\"https://ispc.edu.ar\","
                + "\"categoria\":2,\"asignatura\":1,\"es_plantilla_base\":false,\"copia_de\":3}"));

        Response<Recurso> respuesta = api.detalle(7).execute();

        assertTrue(respuesta.isSuccessful());
        Recurso r = respuesta.body();
        assertNotNull(r);
        assertEquals("<h1>Hola</h1><p>Texto</p>", r.getContenido());
        assertEquals("https://ispc.edu.ar", r.getUrl());
        assertEquals(Integer.valueOf(3), r.getCopiaDe());
        assertEquals("/api/pedagogia/recursos/7/", servidor.takeRequest().getPath());
    }

    @Test
    public void detalle_sinContenidoNiUrl_llegaComoNulosOVacios() throws Exception {
        servidor.enqueue(new MockResponse().setResponseCode(200).setBody("{"
                + "\"id\":8,\"titulo\":\"Vacio\",\"descripcion\":\"\",\"tipo\":\"actividad\","
                + "\"contenido\":\"\",\"url\":null,\"categoria\":1,\"asignatura\":1}"));

        Recurso r = api.detalle(8).execute().body();

        assertNotNull(r);
        assertFalse(ar.edu.ispc.edutools.util.VistaPreviaRecurso.tieneContenido(r));
        assertFalse(ar.edu.ispc.edutools.util.VistaPreviaRecurso.tieneEnlace(r));
        assertTrue(ar.edu.ispc.edutools.util.VistaPreviaRecurso.sinNadaParaMostrar(r));
    }

    @Test
    public void editarPlantilla_mandaLosCamposEditadosYLaCopiaVuelveConEllos() throws Exception {
        // El asesor edita una plantilla base: el servidor responde 201 con la copia propia.
        servidor.enqueue(new MockResponse().setResponseCode(201).setBody("{"
                + "\"id\":21,\"titulo\":\"Mi version\",\"descripcion\":\"Cambiada\",\"tipo\":\"lectura\","
                + "\"contenido\":\"<p>Heredado</p>\",\"url\":\"https://ispc.edu.ar/mia\","
                + "\"categoria\":4,\"asignatura\":2,\"es_plantilla_base\":false,\"copia_de\":5}"));

        Recurso editado = new Recurso(5, "Mi version", "Cambiada", "lectura", 4, 2,
                "https://ispc.edu.ar/mia");
        Response<Recurso> respuesta = api.actualizar(5, editado).execute();

        RecordedRequest pedido = servidor.takeRequest();
        assertEquals("PUT", pedido.getMethod());
        assertEquals("/api/pedagogia/recursos/5/", pedido.getPath());
        String cuerpo = pedido.getBody().readUtf8();
        assertTrue(cuerpo.contains("\"titulo\":\"Mi version\""));
        assertTrue(cuerpo.contains("\"descripcion\":\"Cambiada\""));
        assertTrue(cuerpo.contains("\"url\":\"https://ispc.edu.ar/mia\""));
        assertTrue(cuerpo.contains("\"categoria\":4"));
        assertTrue(cuerpo.contains("\"asignatura\":2"));
        // El contenido no se manda desde el formulario: el servidor lo hereda de la plantilla.
        assertFalse(cuerpo.contains("contenido"));

        assertEquals(201, respuesta.code());
        Recurso copia = respuesta.body();
        assertNotNull(copia);
        assertEquals(21, copia.getId());
        assertEquals("Mi version", copia.getTitulo());
        assertEquals("Cambiada", copia.getDescripcion());
        assertEquals("https://ispc.edu.ar/mia", copia.getUrl());
        assertEquals(4, copia.getCategoria());
        assertEquals(2, copia.getAsignatura());
        assertEquals("<p>Heredado</p>", copia.getContenido());
        assertEquals(Boolean.FALSE, copia.getEsPlantillaBase());
        assertEquals(Integer.valueOf(5), copia.getCopiaDe());
    }
}
