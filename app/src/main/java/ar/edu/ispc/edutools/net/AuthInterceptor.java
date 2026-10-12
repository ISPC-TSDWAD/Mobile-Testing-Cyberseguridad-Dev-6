package ar.edu.ispc.edutools.net;

import java.io.IOException;

import ar.edu.ispc.edutools.security.TokenStorage;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Agrega "Authorization: Bearer <access>" a cada pedido si hay sesion.
 * No registra tokens ni headers en ningun log.
 */
public class AuthInterceptor implements Interceptor {

    private final TokenStorage storage;

    public AuthInterceptor(TokenStorage storage) {
        this.storage = storage;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        String token = storage.getAccessToken();
        if (token == null || token.isEmpty()) {
            return chain.proceed(original);
        }
        Request conToken = original.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(conToken);
    }
}
