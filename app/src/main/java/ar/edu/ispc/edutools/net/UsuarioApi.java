package ar.edu.ispc.edutools.net;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

/** CRUD de usuarios. Todas las rutas terminan en "/". */
public interface UsuarioApi {

    /** Solo admin. Un asesor recibe 403. */
    @GET("api/users/")
    Call<List<UsuarioDto>> listar();

    /** Solo admin. */
    @POST("api/users/")
    Call<UsuarioDto> crear(@Body NuevoUsuarioRequest pedido);

    /** Admin, o el propio usuario. */
    @PATCH("api/users/{id}/")
    Call<UsuarioDto> editar(@Path("id") int id, @Body EditarUsuarioRequest pedido);

    /** Admin, o el propio usuario. Responde 204 sin cuerpo. */
    @DELETE("api/users/{id}/")
    Call<Void> eliminar(@Path("id") int id);
}
