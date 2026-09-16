package ar.edu.ispc.edutools.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Fuente de datos estatica de la app. Replica la estructura y los valores que
 * devuelve la API REST de Django.
 */
public final class DatosDemo {

    private DatosDemo() { }

    public static List<Recurso> listarRecursos() {
        List<Recurso> lista = new ArrayList<>();

        lista.add(new Recurso(1,
                "Video de presentación de la unidad",
                "Componente Video CTA con encabezado institucional y llamada a la acción bajo el reproductor.",
                "video", "Componentes multimedia", "Diseño Instruccional",
                "Daniela Salvo", "https://edutools.edu.ar/plantillas/video-cta"));

        lista.add(new Recurso(2,
                "Acordeón de contenidos ampliatorios",
                "Bloque plegable para organizar lecturas complementarias sin saturar la página del módulo.",
                "acordeon", "Componentes de estructura", "Diseño Instruccional",
                "Ale Corva", "https://edutools.edu.ar/plantillas/acordeon"));

        lista.add(new Recurso(3,
                "Infografía de proceso en cinco pasos",
                "Plantilla responsive para sintetizar procedimientos, con íconos y numeración accesible.",
                "infografia", "Componentes visuales", "Comunicación Visual",
                "Gerardo Romero", "https://edutools.edu.ar/plantillas/infografia"));

        lista.add(new Recurso(4,
                "Consigna de actividad práctica",
                "Estructura de consigna con objetivo, criterios de evaluación y formato de entrega.",
                "actividad", "Componentes de evaluación", "Práctica Profesionalizante",
                "Jonathan Guillén", "https://edutools.edu.ar/plantillas/actividad"));

        lista.add(new Recurso(5,
                "Lectura obligatoria con cita APA",
                "Bloque de lectura con referencia bibliográfica formateada según normas APA 7.",
                "lectura", "Componentes de contenido", "Metodología de la Investigación",
                "Gonzalo Velasco", "https://edutools.edu.ar/plantillas/lectura"));

        lista.add(new Recurso(6,
                "Cuestionario de autoevaluación",
                "Plantilla de autoevaluación formativa con retroalimentación por opción de respuesta.",
                "cuestionario", "Componentes de evaluación", "Diseño Instruccional",
                "Daniela Salvo", "https://edutools.edu.ar/plantillas/cuestionario"));

        return lista;
    }

    public static List<UsuarioItem> listarUsuarios() {
        List<UsuarioItem> lista = new ArrayList<>();
        lista.add(new UsuarioItem(1, "Jonathan Guillén", "jguillen",
                "jguillen@edutools.edu.ar", "admin", true));
        lista.add(new UsuarioItem(2, "Ale Corva", "acorva",
                "acorva@edutools.edu.ar", "maquetador", true));
        lista.add(new UsuarioItem(3, "Gonzalo Velasco", "gvelasco",
                "gvelasco@edutools.edu.ar", "asesor", true));
        lista.add(new UsuarioItem(4, "Daniela Salvo", "dsalvo",
                "dsalvo@edutools.edu.ar", "asesor", true));
        lista.add(new UsuarioItem(5, "Gerardo Romero", "gromero",
                "gromero@edutools.edu.ar", "maquetador", false));
        lista.add(new UsuarioItem(6, "Roni Duncan Gonzales Martínez", "rgonzales",
                "rgonzales@edutools.edu.ar", "asesor", true));
        return lista;
    }

    /** Catalogo de tipos de recurso. */
    public static String[] tiposDeRecurso() {
        return new String[]{
                "Video CTA", "Acordeón", "Infografía",
                "Cuestionario", "Lectura", "Actividad"
        };
    }

    public static String[] categorias() {
        return new String[]{
                "Componentes multimedia", "Componentes de estructura",
                "Componentes visuales", "Componentes de contenido",
                "Componentes de evaluación"
        };
    }

    public static String[] asignaturas() {
        return new String[]{
                "Diseño Instruccional", "Comunicación Visual",
                "Práctica Profesionalizante", "Metodología de la Investigación"
        };
    }
}
