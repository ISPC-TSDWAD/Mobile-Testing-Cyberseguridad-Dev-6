package ar.edu.ispc.edutools.util;

import static org.junit.Assert.assertEquals;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/** Selector de tema: valores, modo de AppCompat, orden de las opciones y persistencia. */
public class TemaAppTest {

    @Test
    public void modoNoche_correspondeALaOpcion() {
        assertEquals(AppCompatDelegate.MODE_NIGHT_NO, TemaApp.modoNoche(TemaApp.CLARO));
        assertEquals(AppCompatDelegate.MODE_NIGHT_YES, TemaApp.modoNoche(TemaApp.OSCURO));
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, TemaApp.modoNoche(TemaApp.SISTEMA));
    }

    @Test
    public void valorDesconocidoOVacioEquivaleASegunElSistema() {
        assertEquals(TemaApp.SISTEMA, TemaApp.normalizar(null));
        assertEquals(TemaApp.SISTEMA, TemaApp.normalizar(""));
        assertEquals(TemaApp.SISTEMA, TemaApp.normalizar("violeta"));
        assertEquals(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, TemaApp.modoNoche("violeta"));
    }

    @Test
    public void opciones_siguenElOrdenClaroOscuroSistema() {
        assertEquals(0, TemaApp.indice(TemaApp.CLARO));
        assertEquals(1, TemaApp.indice(TemaApp.OSCURO));
        assertEquals(2, TemaApp.indice(TemaApp.SISTEMA));
        assertEquals(TemaApp.CLARO, TemaApp.valorEn(0));
        assertEquals(TemaApp.OSCURO, TemaApp.valorEn(1));
        assertEquals(TemaApp.SISTEMA, TemaApp.valorEn(2));
    }

    @Test
    public void indiceFueraDeRango_devuelveSegunElSistema() {
        assertEquals(TemaApp.SISTEMA, TemaApp.valorEn(-1));
        assertEquals(TemaApp.SISTEMA, TemaApp.valorEn(3));
        assertEquals(2, TemaApp.indice(null));
    }

    @Test
    public void sinPreferenciaGuardada_empiezaSegunElSistema() {
        assertEquals(TemaApp.SISTEMA, TemaApp.leer(new PrefsEnMemoria()));
    }

    @Test
    public void guardarYLeer_conservaLaEleccion() {
        PrefsEnMemoria prefs = new PrefsEnMemoria();
        TemaApp.guardar(prefs, TemaApp.OSCURO);
        assertEquals(TemaApp.OSCURO, TemaApp.leer(prefs));
        TemaApp.guardar(prefs, TemaApp.CLARO);
        assertEquals(TemaApp.CLARO, TemaApp.leer(prefs));
    }

    @Test
    public void valorInvalidoGuardado_seLeeComoSegunElSistema() {
        PrefsEnMemoria prefs = new PrefsEnMemoria();
        prefs.datos.put("tema", "rosa");
        assertEquals(TemaApp.SISTEMA, TemaApp.leer(prefs));
        TemaApp.guardar(prefs, "rosa");
        assertEquals(TemaApp.SISTEMA, TemaApp.leer(prefs));
    }

    /** SharedPreferences minimo para probar sin Android. */
    private static class PrefsEnMemoria implements SharedPreferences {
        final Map<String, Object> datos = new HashMap<>();

        @Override
        public String getString(String clave, String porDefecto) {
            Object valor = datos.get(clave);
            return valor instanceof String ? (String) valor : porDefecto;
        }

        @Override
        public Editor edit() {
            return new Editor() {
                private final Map<String, Object> pendiente = new HashMap<>();

                @Override
                public Editor putString(String clave, String valor) {
                    pendiente.put(clave, valor);
                    return this;
                }

                @Override
                public void apply() {
                    datos.putAll(pendiente);
                }

                @Override
                public boolean commit() {
                    apply();
                    return true;
                }

                @Override public Editor putStringSet(String c, Set<String> v) { return this; }
                @Override public Editor putInt(String c, int v) { return this; }
                @Override public Editor putLong(String c, long v) { return this; }
                @Override public Editor putFloat(String c, float v) { return this; }
                @Override public Editor putBoolean(String c, boolean v) { return this; }
                @Override public Editor remove(String c) { return this; }
                @Override public Editor clear() { return this; }
            };
        }

        @Override public Map<String, ?> getAll() { return datos; }
        @Override public Set<String> getStringSet(String c, Set<String> d) { return d; }
        @Override public int getInt(String c, int d) { return d; }
        @Override public long getLong(String c, long d) { return d; }
        @Override public float getFloat(String c, float d) { return d; }
        @Override public boolean getBoolean(String c, boolean d) { return d; }
        @Override public boolean contains(String c) { return datos.containsKey(c); }
        @Override public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener l) { }
        @Override public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener l) { }
    }
}
