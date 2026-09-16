package ar.edu.ispc.edutools.model;

import java.io.Serializable;

/** Modelo de usuario del sistema. Espeja el modelo Usuario del backend Django. */
public class UsuarioItem implements Serializable {

    private final int id;
    private final String nombreCompleto;
    private final String username;
    private final String correo;
    private final String rol;       // admin | asesor | maquetador
    private final boolean activo;

    public UsuarioItem(int id, String nombreCompleto, String username,
                       String correo, String rol, boolean activo) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.correo = correo;
        this.rol = rol;
        this.activo = activo;
    }

    public int getId() { return id; }

    public String getNombreCompleto() { return nombreCompleto; }

    public String getUsername() { return username; }

    public String getCorreo() { return correo; }

    public String getRol() { return rol; }

    public boolean isActivo() { return activo; }

    /** Etiqueta legible del rol. */
    public String getRolLegible() {
        switch (rol) {
            case "admin":      return "Administrador";
            case "asesor":     return "Asesor Pedagógico";
            case "maquetador": return "Maquetador";
            default:           return rol;
        }
    }

    public String getEstadoLegible() {
        return activo ? "Activo" : "Inactivo";
    }
}
