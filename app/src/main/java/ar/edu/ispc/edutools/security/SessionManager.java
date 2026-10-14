package ar.edu.ispc.edutools.security;

import android.content.Context;

import ar.edu.ispc.edutools.util.JwtUtil;

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

    /** Guarda el usuario para saludarlo al reabrir la app con la sesion abierta. */
    public void guardarUsuario(String usuario) {
        storage.saveUsername(usuario);
    }

    public String getUsuario() {
        return storage.getUsername();
    }

    /** Hay sesion guardada y el access token todavia no vencio. */
    public boolean haySesionValida() {
        return estaLogueado() && !JwtUtil.estaVencido(storage.getAccessToken());
    }

    public String getAccessToken() {
        return storage.getAccessToken();
    }

    public void cerrarSesion() {
        storage.clear();
    }
}
