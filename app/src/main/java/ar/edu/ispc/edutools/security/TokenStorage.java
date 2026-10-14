package ar.edu.ispc.edutools.security;

/**
 * Contrato para guardar los tokens JWT de la sesion.
 * TokenStorageProvider usa almacenamiento cifrado desde API 23 y memoria
 * en API 21/22 o cuando el Keystore no esta disponible.
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
