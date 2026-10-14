package ar.edu.ispc.edutools.security;

/** Guarda los tokens solo en memoria: se pierden al cerrar la app. */
public class MemoryTokenStorage implements TokenStorage {

    private String access;
    private String refresh;
    private String username;

    @Override
    public synchronized void saveTokens(String access, String refresh) {
        this.access = access;
        this.refresh = refresh;
    }

    @Override
    public synchronized String getAccessToken() {
        return access;
    }

    @Override
    public synchronized String getRefreshToken() {
        return refresh;
    }

    @Override
    public synchronized void saveUsername(String username) {
        this.username = username;
    }

    @Override
    public synchronized String getUsername() {
        return username;
    }

    @Override
    public synchronized void clear() {
        access = null;
        refresh = null;
        username = null;
    }
}
