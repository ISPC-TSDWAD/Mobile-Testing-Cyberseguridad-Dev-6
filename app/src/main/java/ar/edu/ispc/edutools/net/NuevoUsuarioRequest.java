package ar.edu.ispc.edutools.net;

import com.google.gson.annotations.SerializedName;

/** Cuerpo del POST api/users/ (solo admin). El rol no se manda: lo cambia set-role. */
public class NuevoUsuarioRequest {

    private final String username;
    private final String password;
    private final String email;

    @SerializedName("first_name")
    private final String firstName;

    @SerializedName("last_name")
    private final String lastName;

    public NuevoUsuarioRequest(String username, String password, String email,
                               String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
