package ar.edu.ispc.edutools.net;

import com.google.gson.annotations.SerializedName;

import ar.edu.ispc.edutools.model.UsuarioItem;

/** Usuario tal como lo devuelve la API (api/users/). La contrasena nunca viene. */
public class UsuarioDto {

    private int id;
    private String username;
    private String email;

    @SerializedName("first_name")
    private String firstName;

    @SerializedName("last_name")
    private String lastName;

    @SerializedName("is_active")
    private boolean isActive;

    private String rol;

    /** Lo pasa al modelo que ya usan las pantallas. */
    public UsuarioItem toItem() {
        String nombre = firstName == null ? "" : firstName;
        String apellido = lastName == null ? "" : lastName;
        String completo = (nombre + " " + apellido).trim();
        if (completo.isEmpty()) {
            completo = username;   // si no cargo nombre, se muestra el usuario
        }
        return new UsuarioItem(id, completo, username, email == null ? "" : email,
                rol, isActive, nombre, apellido);
    }
}
