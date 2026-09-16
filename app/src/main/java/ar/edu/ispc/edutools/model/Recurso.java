package ar.edu.ispc.edutools.model;

import java.io.Serializable;

/**
 * Modelo de recurso pedagogico. Espeja el modelo del backend Django.
 * Es Serializable porque viaja entre Activity dentro del Intent.
 */
public class Recurso implements Serializable {

    private final int id;
    private final String titulo;
    private final String descripcion;
    private final String tipo;          // video | acordeon | infografia | cuestionario | lectura | actividad
    private final String categoria;
    private final String asignatura;
    private final String creadoPor;
    private final String url;

    public Recurso(int id, String titulo, String descripcion, String tipo,
                   String categoria, String asignatura, String creadoPor, String url) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.asignatura = asignatura;
        this.creadoPor = creadoPor;
        this.url = url;
    }

    public int getId() { return id; }

    public String getTitulo() { return titulo; }

    public String getDescripcion() { return descripcion; }

    public String getTipo() { return tipo; }

    public String getCategoria() { return categoria; }

    public String getAsignatura() { return asignatura; }

    public String getCreadoPor() { return creadoPor; }

    public String getUrl() { return url; }

    /** Etiqueta legible del tipo de recurso. */
    public String getTipoLegible() {
        switch (tipo) {
            case "video":        return "Video CTA";
            case "acordeon":     return "Acordeón";
            case "infografia":   return "Infografía";
            case "cuestionario": return "Cuestionario";
            case "lectura":      return "Lectura";
            case "actividad":    return "Actividad";
            default:             return tipo;
        }
    }
}
