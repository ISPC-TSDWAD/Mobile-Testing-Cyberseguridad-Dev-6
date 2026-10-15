package ar.edu.ispc.edutools;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.security.SessionManager;

/** ACC-02: ATF sobre las pantallas principales, sin suprimir errores. */
@RunWith(AndroidJUnit4.class)
public class AccesibilidadPantallasTest {
    private void revisar(Class<? extends BaseActivity> pantalla) {
        try (ActivityScenario<? extends BaseActivity> scenario = ActivityScenario.launch(pantalla)) {
            scenario.onActivity(activity -> AccessibilityChecks.enable()
                    .setRunChecksFromRootView(true).check(activity.findViewById(android.R.id.content)));
        }
    }

    @Test public void login_sinErroresATF() {
        new SessionManager(ApplicationProvider.<android.content.Context>getApplicationContext()).cerrarSesion();
        revisar(LoginActivity.class);
    }
    @Test public void home_sinErroresATF() { revisar(MainActivity.class); }
    @Test public void recursos_sinErroresATF() { revisar(RecursosActivity.class); }
    @Test public void formulario_sinErroresATF() { revisar(RecursoFormActivity.class); }
    @Test public void usuarios_sinErroresATF() { revisar(UsuariosActivity.class); }
    @Test public void perfil_sinErroresATF() { revisar(PerfilUsuarioActivity.class); }
    @Test public void registro_sinErroresATF() { revisar(RegistroActivity.class); }
    @Test public void multimedia_sinErroresATF() { revisar(MultimediaActivity.class); }
    @Test public void contacto_sinErroresATF() { revisar(ContactoActivity.class); }
    @Test public void acerca_sinErroresATF() { revisar(AcercaDeActivity.class); }

    @Test public void detalle_sinErroresATF() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RecursoDetalleActivity.class);
        intent.putExtra(RecursosActivity.EXTRA_RECURSO, new Recurso(
                99999999, "Recurso de prueba visual", "Descripcion de prueba", "lectura",
                1, 1, "Categoria", "Asignatura", null, "https://example.com"));
        try (ActivityScenario<RecursoDetalleActivity> scenario = ActivityScenario.launch(intent)) {
            scenario.onActivity(activity -> AccessibilityChecks.enable()
                    .setRunChecksFromRootView(true).check(activity.findViewById(android.R.id.content)));
        }
    }
}
