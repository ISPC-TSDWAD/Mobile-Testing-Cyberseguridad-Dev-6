package ar.edu.ispc.edutools.net;

/** Cuerpo del POST api/token/. */
public class LoginRequest {

    private final String username;
    private final String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
