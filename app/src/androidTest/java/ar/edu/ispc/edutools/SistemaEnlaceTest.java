package ar.edu.ispc.edutools;

import android.content.Intent;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.security.SessionManager;
import ar.edu.ispc.edutools.screens.NavegadorScreen;
import static org.junit.Assume.assumeTrue;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

/** AUT-SYS-01: confirma el destino HTTPS, abre otra app y vuelve al recurso. */
@RunWith(AndroidJUnit4.class)
public class SistemaEnlaceTest {
    @BeforeClass public static void preparar() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
        String token = InstrumentationRegistry.getArguments().getString("testAccessToken");
        assumeTrue("Se requiere una cuenta de prueba configurada", token != null);
        new SessionManager(ApplicationProvider.<android.content.Context>getApplicationContext())
                .guardarSesion(token, "");
    }

    @Test public void enlaceHttps_confirmaAbreNavegadorYRegresa() throws Exception {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), RecursoDetalleActivity.class);
        intent.putExtra(RecursosActivity.EXTRA_RECURSO, new Recurso(
                99999999, "Recurso con enlace", "Prueba de navegacion externa", "lectura",
                1, 1, "Categoria", "Asignatura", null, "https://example.com"));
        try (ActivityScenario<RecursoDetalleActivity> scenario = ActivityScenario.launch(intent)) {
            onView(withId(R.id.btnAbrirEnlace)).perform(scrollTo(), click());
            onView(withText(R.string.preview_abrir)).perform(click());
            new NavegadorScreen(UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()))
                    .verificarAperturaYVolver("ar.edu.ispc.edutools");
            onView(withId(R.id.tvDetalleTitulo)).check(matches(withText("Recurso con enlace")));
        }
    }
}
