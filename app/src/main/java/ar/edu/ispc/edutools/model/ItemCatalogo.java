package ar.edu.ispc.edutools.model;

import java.io.Serializable;

/**
 * Elemento de catálogo de Pedagogía (Categoría o Asignatura) obtenido de la API.
 * Su toString() devuelve el nombre para mostrarse directamente en los Spinner.
 */
public class ItemCatalogo implements Serializable {

    private int id;
    private String nombre;

    public ItemCatalogo() { }

    public ItemCatalogo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre != null ? nombre : "";
    }

    @Override
    public String toString() {
        return getNombre();
    }
}
