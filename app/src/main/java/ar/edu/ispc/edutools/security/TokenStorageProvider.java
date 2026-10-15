package ar.edu.ispc.edutools.security;

import android.content.Context;
import android.os.Build;

/**
 * Unico lugar donde se decide que TokenStorage usa la app.
 * En Android 6 o mas se guardan cifrados (EncryptedTokenStorage, Issue #26).
 * En Android 5 (API 21 y 22) no hay soporte, asi que se usa memoria:
 * la sesion no sobrevive al cierre de la app, pero el token nunca queda en texto plano.
 */
public final class TokenStorageProvider {

    private static TokenStorage instancia;

    private TokenStorageProvider() { }

    public static synchronized TokenStorage get(Context context) {
        if (instancia == null) {
            instancia = crear(context);
        }
        return instancia;
    }

    private static TokenStorage crear(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                return new EncryptedTokenStorage(context);
            } catch (Exception e) {
                // Si falla el Keystore se usa memoria: nunca se cae a texto plano.
            }
        }
        return new MemoryTokenStorage();
    }
}
