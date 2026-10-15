package ar.edu.ispc.edutools.util;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

/**
 * Tema de la app (Claro / Oscuro / Según el sistema).
 *
 * La preferencia no es sensible, asi que se guarda en SharedPreferences comunes
 * (el token sigue en EncryptedSharedPreferences). Se aplica al iniciar el proceso
 * desde EduToolsApp y al elegir una opcion; AppCompat recrea las pantallas abiertas,
 * de modo que el cambio alcanza a todas.
 */
public final class TemaApp {

    public static final String CLARO = "claro";
    public static final String OSCURO = "oscuro";
    public static final String SISTEMA = "sistema";

    /** Orden en que se muestran las opciones: Claro, Oscuro, Segun el sistema. */
    private static final String[] VALORES = {CLARO, OSCURO, SISTEMA};

    private static final String ARCHIVO = "edutools_ajustes";
    private static final String CLAVE_TEMA = "tema";

    private TemaApp() {
    }

    /** Cualquier valor desconocido o vacio equivale a "Segun el sistema". */
    public static String normalizar(String valor) {
        if (CLARO.equals(valor) || OSCURO.equals(valor)) {
            return valor;
        }
        return SISTEMA;
    }

    /** Modo de AppCompat que corresponde al valor guardado. */
    public static int modoNoche(String valor) {
        switch (normalizar(valor)) {
            case CLARO:
                return AppCompatDelegate.MODE_NIGHT_NO;
            case OSCURO:
                return AppCompatDelegate.MODE_NIGHT_YES;
            default:
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM;
        }
    }

    /** Posicion de un valor en la lista de opciones. */
    public static int indice(String valor) {
        String normal = normalizar(valor);
        for (int i = 0; i < VALORES.length; i++) {
            if (VALORES[i].equals(normal)) {
                return i;
            }
        }
        return VALORES.length - 1;
    }

    /** Valor de la opcion elegida en la lista; fuera de rango devuelve "sistema". */
    public static String valorEn(int indice) {
        if (indice < 0 || indice >= VALORES.length) {
            return SISTEMA;
        }
        return VALORES[indice];
    }

    public static String leer(SharedPreferences prefs) {
        return normalizar(prefs.getString(CLAVE_TEMA, SISTEMA));
    }

    public static void guardar(SharedPreferences prefs, String valor) {
        prefs.edit().putString(CLAVE_TEMA, normalizar(valor)).apply();
    }

    public static String leer(Context contexto) {
        return leer(prefs(contexto));
    }

    /** Aplica el tema guardado. Se llama una vez al iniciar la app. */
    public static void aplicar(Context contexto) {
        AppCompatDelegate.setDefaultNightMode(modoNoche(leer(contexto)));
    }

    /** Guarda la eleccion y la aplica en el momento. */
    public static void elegir(Context contexto, String valor) {
        guardar(prefs(contexto), valor);
        aplicar(contexto);
    }

    private static SharedPreferences prefs(Context contexto) {
        return contexto.getApplicationContext().getSharedPreferences(ARCHIVO, Context.MODE_PRIVATE);
    }
}
