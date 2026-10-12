package ar.edu.ispc.edutools.net;

import com.google.gson.annotations.SerializedName;

/** Cuerpo del PATCH api/users/{id}/. Solo datos de perfil; el rol y is_staff el servidor los ignora. */
public class EditarUsuarioRequest {

    @SerializedName("first_name")
    private final String firstName;

    @SerializedName("last_name")
    private final String lastName;

    private final String email;

    public EditarUsuarioRequest(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
