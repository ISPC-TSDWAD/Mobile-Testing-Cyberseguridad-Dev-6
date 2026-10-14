package ar.edu.ispc.edutools.util;

import ar.edu.ispc.edutools.model.ItemCatalogo;

/**
 * Resuelve que id de catalogo (categoria o asignatura) se manda al guardar un recurso.
 * Nunca inventa un valor: si no hay ni seleccion ni dato previo devuelve SIN_ID.
 */
public final class CatalogoUtil {

    public static final int SIN_ID = -1;

    private CatalogoUtil() { }

    /**
     * @param seleccionado   lo que hay elegido en el Spinner (puede no ser un ItemCatalogo)
     * @param idDelRecurso   id que ya tenia el recurso al editarlo (0 o menos si es alta)
     */
    public static int resolverId(Object seleccionado, int idDelRecurso) {
        if (seleccionado instanceof ItemCatalogo) {
            return ((ItemCatalogo) seleccionado).getId();
        }
        return idDelRecurso > 0 ? idDelRecurso : SIN_ID;
    }
}
