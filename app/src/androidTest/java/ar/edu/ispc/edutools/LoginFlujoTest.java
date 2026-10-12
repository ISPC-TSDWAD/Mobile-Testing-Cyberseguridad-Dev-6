package ar.edu.ispc.edutools;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ar.edu.ispc.edutools.screens.LoginScreen;

/**
 * AUT-UI-01 · Flujo critico: Acceso con campos vacios (HU de login, Issue #24).
 * Con AccessibilityChecks activo, cada accion tambien audita la pantalla (ACC).
 */
@RunWith(AndroidJUnit4.class)
public class LoginFlujoTest {

    private static final String OBLIGATORIO = "Este campo es obligatorio.";

    @BeforeClass
    public static void activarChequeosDeAccesibilidad() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    @Rule
    public ActivityScenarioRule<LoginActivity> regla =
            new ActivityScenarioRule<>(LoginActivity.class);

    @Test
    public void loginVacio_marcaErrorEnUsuarioYContrasena_yNoAvanza() {
        // Arrange: pantalla de acceso vacia (la abre la regla)
        LoginScreen login = new LoginScreen();
        // Act
        login.tocarIngresar();
        // Assert
        login.verificarErrorUsuario(OBLIGATORIO)
                .verificarErrorPassword(OBLIGATORIO)
                .verificarSigueEnLogin();
    }

    @Test
    public void loginSoloConUsuario_marcaErrorSoloEnContrasena() {
        LoginScreen login = new LoginScreen();
        login.escribirUsuario("goni").tocarIngresar();
        login.verificarUsuarioSinError().verificarErrorPassword(OBLIGATORIO);
    }
}
