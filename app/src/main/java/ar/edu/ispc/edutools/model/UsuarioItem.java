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
    private final String nombre;     // first_name, se usa para editar
    private final String apellido;   // last_name, se usa para editar

    public UsuarioItem(int id, String nombreCompleto, String username,
                       String correo, String rol, boolean activo) {
        this(id, nombreCompleto, username, correo, rol, activo, nombreCompleto, "");
    }

    public UsuarioItem(int id, String nombreCompleto, String username,
                       String correo, String rol, boolean activo,
                       String nombre, String apellido) {
        this.id = id;
        this.nombreCompleto = nombreCompleto;
        this.username = username;
        this.correo = correo;
        this.rol = rol;
        this.activo = activo;
        this.nombre = nombre;
        this.apellido = apellido;
    }

    public int getId() { return id; }

    public String getNombreCompleto() { return nombreCompleto; }

    public String getUsername() { return username; }

    public String getCorreo() { return correo; }

    public String getRol() { return rol; }

    public boolean isActivo() { return activo; }

    public String getNombre() { return nombre; }

    public String getApellido() { return apellido; }

    /** Etiqueta legible del rol. */
    public String getRolLegible() {
        return nombreDeRol(rol);
    }

    /** Pasa el rol del backend (admin, asesor, maquetador) a texto para mostrar. */
    public static String nombreDeRol(String rol) {
        if (rol == null) {
            return "Sin rol";
        }
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
