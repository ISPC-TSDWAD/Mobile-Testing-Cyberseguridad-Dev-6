package ar.edu.ispc.edutools.net;

import java.util.List;

import ar.edu.ispc.edutools.model.Recurso;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * CRUD de recursos pedagógicos contra la API de Pedagogía (api/pedagogia/recursos/).
 * Todas las rutas terminan en "/".
 */
public interface RecursoApi {

    /** Lista los recursos pedagógicos disponibles según el rol del usuario autenticado. */
    @GET("api/pedagogia/recursos/")
    Call<List<Recurso>> listar();

    /** Obtiene el detalle de un recurso por su ID. */
    @GET("api/pedagogia/recursos/{id}/")
    Call<Recurso> detalle(@Path("id") int id);

    /** Crea un nuevo recurso pedagógico. */
    @POST("api/pedagogia/recursos/")
    Call<Recurso> crear(@Body Recurso recurso);

    /** Actualiza un recurso pedagógico existente por su ID. */
    @PUT("api/pedagogia/recursos/{id}/")
    Call<Recurso> actualizar(@Path("id") int id, @Body Recurso recurso);

    /** Elimina un recurso pedagógico por su ID. Responde 204 No Content sin cuerpo. */
    @DELETE("api/pedagogia/recursos/{id}/")
    Call<Void> eliminar(@Path("id") int id);
}
