package ar.edu.ispc.edutools.net;

import android.content.Context;

import java.util.concurrent.TimeUnit;

import ar.edu.ispc.edutools.security.TokenStorage;
import ar.edu.ispc.edutools.security.TokenStorageProvider;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/** Crea los servicios Retrofit ya configurados con HTTPS, timeouts y token. */
public final class ApiClient {

    private static final int TIMEOUT_SEGUNDOS = 15;

    private ApiClient() { }

    /** Uso normal en la app: URL de ApiConfig y TokenStorage del provider. */
    public static <T> T create(Context context, Class<T> servicio) {
        return create(ApiConfig.BASE_URL, TokenStorageProvider.get(context), servicio);
    }

    /** Version para tests (MockWebServer) o para cambiar de servidor. */
    public static <T> T create(String baseUrl, TokenStorage storage, Class<T> servicio) {
        OkHttpClient cliente = new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_SEGUNDOS, TimeUnit.SECONDS)
                .addInterceptor(new AuthInterceptor(storage))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(cliente)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(servicio);
    }
}
