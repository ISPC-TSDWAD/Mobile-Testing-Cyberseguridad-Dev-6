package ar.edu.ispc.edutools;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ar.edu.ispc.edutools.ErrorEnCampo.conError;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import ar.edu.ispc.edutools.security.SessionManager;
import static org.junit.Assume.assumeTrue;

/** AUT-UI-04, TC-26: un titulo vacio impide enviar el alta de un recurso. */
@RunWith(AndroidJUnit4.class)
public class RecursoFormFlujoTest {
    @BeforeClass
    public static void activarChequeos() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
        String token = InstrumentationRegistry.getArguments().getString("testAccessToken");
        assumeTrue("El formulario protegido requiere una cuenta de prueba configurada", token != null);
        new SessionManager(ApplicationProvider.<android.content.Context>getApplicationContext())
                .guardarSesion(token, "");
    }

    @Rule
    public ActivityScenarioRule<RecursoFormActivity> regla = new ActivityScenarioRule<>(RecursoFormActivity.class);

    @Test
    public void tituloVacio_marcaErrorYConservaFormulario() {
        onView(withId(R.id.btnGuardarRecurso)).perform(scrollTo(), click());
        onView(withId(R.id.tilFormTitulo)).check(matches(conError(
                "El título es obligatorio y no puede superar los 200 caracteres.")));
    }
}
