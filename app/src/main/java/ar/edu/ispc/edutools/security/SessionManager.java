package ar.edu.ispc.edutools.security;

import android.content.Context;

/** Maneja la sesion del usuario (tokens JWT) sin exponer donde se guardan. */
public class SessionManager {

    private final TokenStorage storage;

    public SessionManager(Context context) {
        this.storage = TokenStorageProvider.get(context);
    }

    /** Para los tests: se le pasa el almacenamiento directamente. */
    public SessionManager(TokenStorage storage) {
        this.storage = storage;
    }

    public void guardarSesion(String access, String refresh) {
        storage.saveTokens(access, refresh);
    }

    public boolean estaLogueado() {
        String token = storage.getAccessToken();
        return token != null && !token.isEmpty();
    }

    public String getAccessToken() {
        return storage.getAccessToken();
    }

    public void cerrarSesion() {
        storage.clear();
    }
}
