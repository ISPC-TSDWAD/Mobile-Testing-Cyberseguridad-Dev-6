package ar.edu.ispc.edutools.security;

/**
 * Contrato para guardar los tokens JWT de la sesion.
 * Hoy se usa MemoryTokenStorage; la version cifrada (EncryptedSharedPreferences)
 * se enchufa cambiando una sola linea en TokenStorageProvider.
 */
public interface TokenStorage {

    void saveTokens(String access, String refresh);

    String getAccessToken();

    String getRefreshToken();

    /** Guarda el nombre de usuario de la sesion (para mostrarlo en el Home al reabrir la app). */
    default void saveUsername(String username) { }

    default String getUsername() {
        return null;
    }

    void clear();
}
