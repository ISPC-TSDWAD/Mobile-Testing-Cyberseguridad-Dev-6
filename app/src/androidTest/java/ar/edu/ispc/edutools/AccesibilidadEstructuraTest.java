package ar.edu.ispc.edutools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/** HUM-09: estructura de lectura y anuncio de resultados dinamicos. */
@RunWith(AndroidJUnit4.class)
public class AccesibilidadEstructuraTest {
    @BeforeClass
    public static void activarChequeos() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    @Rule
    public ActivityScenarioRule<MainActivity> regla = new ActivityScenarioRule<>(MainActivity.class);

    @Test
    public void home_titulosSeIdentificanComoEncabezados() {
        regla.getScenario().onActivity(activity -> {
            assertTrue(ViewCompat.isAccessibilityHeading(activity.findViewById(R.id.tvBienvenida)));
            assertTrue(ViewCompat.isAccessibilityHeading(activity.findViewById(R.id.tvTituloMenu)));
        });
    }

    @Test
    public void resultadosDeListados_tienenAnuncioPolite() {
        regla.getScenario().onActivity(activity -> {
            View recursos = activity.getLayoutInflater().inflate(R.layout.activity_recursos, null);
            View usuarios = activity.getLayoutInflater().inflate(R.layout.activity_usuarios, null);
            assertEquals(View.ACCESSIBILITY_LIVE_REGION_POLITE,
                    recursos.findViewById(R.id.tvCantidadRecursos).getAccessibilityLiveRegion());
            assertEquals(View.ACCESSIBILITY_LIVE_REGION_POLITE,
                    recursos.findViewById(R.id.tvEstadoRecursos).getAccessibilityLiveRegion());
            assertEquals(View.ACCESSIBILITY_LIVE_REGION_POLITE,
                    usuarios.findViewById(R.id.tvCantidadUsuarios).getAccessibilityLiveRegion());
        });
    }
}
