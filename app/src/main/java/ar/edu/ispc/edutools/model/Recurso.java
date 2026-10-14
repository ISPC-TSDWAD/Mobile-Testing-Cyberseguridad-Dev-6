package ar.edu.ispc.edutools.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Modelo de recurso pedagógico. Espeja el JSON del backend Django (api/pedagogia/recursos/).
 * Es Serializable porque viaja entre Activity dentro del Intent.
 */
public class Recurso implements Serializable {

    private int id;
    private String titulo;
    private String descripcion;
    private String tipo;          // video | acordeon | infografia | cuestionario | lectura | actividad
    private String contenido;
    private String url;
    private int categoria;
    private int asignatura;

    @SerializedName("categoria_nombre")
    private String categoriaNombre;

    @SerializedName("asignatura_nombre")
    private String asignaturaNombre;

    @SerializedName("creado_por")
    private Integer creadoPor;

    @SerializedName("es_plantilla_base")
    private Boolean esPlantillaBase;

    @SerializedName("copia_de")
    private Integer copiaDe;

    private Boolean activo;

    /** Constructor completo (usado en tests y por la API). */
    public Recurso(int id, String titulo, String descripcion, String tipo,
                   int categoria, int asignatura, String categoriaNombre,
                   String asignaturaNombre, Integer creadoPor, String url) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.tipo = tipo;
        this.categoria = categoria;
        this.asignatura = asignatura;
        this.categoriaNombre = categoriaNombre;
        this.asignaturaNombre = asignaturaNombre;
        this.creadoPor = creadoPor;
        this.url = url;
    }

    /** Constructor para enviar a la API (crear o editar). Creado_por no se envía (queda null). */
    public Recurso(int id, String titulo, String descripcion, String tipo,
                   int categoria, int asignatura, String url) {
        this(id, titulo, descripcion, tipo, categoria, asignatura, null, null, null, url);
    }

    /** Constructor para crear recurso nuevo (sin id). */
    public Recurso(String titulo, String descripcion, String tipo,
                   int categoria, int asignatura, String url) {
        this(0, titulo, descripcion, tipo, categoria, asignatura, null, null, null, url);
    }

    /** Constructor de compatibilidad para datos demo / pruebas anteriores. */
    public Recurso(int id, String titulo, String descripcion, String tipo,
                   String categoriaNombre, String asignaturaNombre, String creadoPor, String url) {
        this(id, titulo, descripcion, tipo, 1, 1, categoriaNombre, asignaturaNombre, null, url);
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getTipo() {
        return tipo;
    }

    public String getContenido() {
        return contenido;
    }

    public String getUrl() {
        return url;
    }

    public int getCategoria() {
        return categoria;
    }

    public int getAsignatura() {
        return asignatura;
    }

    public String getCategoriaNombre() {
        return categoriaNombre != null ? categoriaNombre : "";
    }

    public String getAsignaturaNombre() {
        return asignaturaNombre != null ? asignaturaNombre : "";
    }

    public Integer getCreadoPor() {
        return creadoPor;
    }

    public Boolean getEsPlantillaBase() {
        return esPlantillaBase;
    }

    public Integer getCopiaDe() {
        return copiaDe;
    }

    public Boolean getActivo() {
        return activo;
    }

    /** Etiqueta legible del tipo de recurso. */
    public String getTipoLegible() {
        if (tipo == null) {
            return "";
        }
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
