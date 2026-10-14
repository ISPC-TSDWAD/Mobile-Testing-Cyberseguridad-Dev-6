package ar.edu.ispc.edutools.net;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Callback comun para las pantallas: separa el caso bueno del error y
 * deja el mensaje de error ya traducido con ApiErrorMapper.
 */
public abstract class ApiCallback<T> implements Callback<T> {

    /** La API respondio 2xx. En un 204 el cuerpo es null. */
    public abstract void onExito(T cuerpo);

    /** Mensaje en espanol listo para mostrar. */
    public abstract void onError(String mensaje);

    /**
     * La API respondio 401. Por defecto se trata como un error comun (asi el Login
     * muestra "usuario o clave incorrectos"); las pantallas con sesion lo
     * sobreescriben para cerrar la sesion y volver al Login.
     */
    public void onSesionVencida(String mensaje) {
        onError(mensaje);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> respuesta) {
        if (respuesta.isSuccessful()) {
            onExito(respuesta.body());
            return;
        }
        String cuerpoError = null;
        try {
            if (respuesta.errorBody() != null) {
                cuerpoError = respuesta.errorBody().string();
            }
        } catch (IOException e) {
            // Si no se puede leer el cuerpo, igual se muestra el mensaje por codigo.
        }
        String mensaje = ApiErrorMapper.mensaje(respuesta.code(), cuerpoError);
        if (respuesta.code() == 401) {
            onSesionVencida(mensaje);
            return;
        }
        onError(mensaje);
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onError(ApiErrorMapper.mensajeSinRed());
    }
}
