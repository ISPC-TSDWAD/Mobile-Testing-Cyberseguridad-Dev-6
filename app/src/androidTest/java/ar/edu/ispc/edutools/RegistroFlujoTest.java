package ar.edu.ispc.edutools;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ar.edu.ispc.edutools.screens.RegistroScreen;

/**
 * AUT-UI-03 · Flujo critico: Registro con contrasenas distintas (Issues #23 y #24).
 */
@RunWith(AndroidJUnit4.class)
public class RegistroFlujoTest {

    @BeforeClass
    public static void activarChequeosDeAccesibilidad() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    @Rule
    public ActivityScenarioRule<RegistroActivity> regla =
            new ActivityScenarioRule<>(RegistroActivity.class);

    @Test
    public void registroConContrasenasDistintas_marcaErrorEnLaRepeticion() {
        // Arrange
        RegistroScreen registro = new RegistroScreen().completar(
                "gvelasco", "Gonzalo", "Velasco", "goni@ispc.edu.ar", "Clave2026", "Otra2026");
        // Act
        registro.tocarRegistrar();
        // Assert
        registro.verificarErrorRepeticion("Las contraseñas no coinciden.")
                .verificarSinErrorEnUsuarioYEmail()
                .verificarSigueEnRegistro();
    }
}
