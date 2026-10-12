package ar.edu.ispc.edutools.net;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/** Endpoint de login. La ruta termina en "/" igual que en el backend. */
public interface AuthApi {

    @POST("api/token/")
    Call<LoginResponse> login(@Body LoginRequest pedido);
}
