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

    void clear();
}
