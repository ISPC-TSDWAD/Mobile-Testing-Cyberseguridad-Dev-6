package ar.edu.ispc.edutools;

import android.app.Application;

import ar.edu.ispc.edutools.util.TemaApp;

/**
 * Se ejecuta antes que cualquier pantalla: aplica el tema elegido por el usuario,
 * de modo que todas las Activity (incluido el Login) lo respeten.
 */
public class EduToolsApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TemaApp.aplicar(this);
    }
}
