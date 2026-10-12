package ar.edu.ispc.edutools.security;

import android.content.Context;

/**
 * Unico lugar donde se decide que TokenStorage usa la app.
 * Para pasar al almacenamiento cifrado solo hay que cambiar la linea marcada.
 */
public final class TokenStorageProvider {

    private static TokenStorage instancia;

    private TokenStorageProvider() { }

    public static synchronized TokenStorage get(Context context) {
        if (instancia == null) {
            // CAMBIAR ESTA LINEA por EncryptedTokenStorage cuando exista (Issue #26).
            instancia = new MemoryTokenStorage();
        }
        return instancia;
    }
}
