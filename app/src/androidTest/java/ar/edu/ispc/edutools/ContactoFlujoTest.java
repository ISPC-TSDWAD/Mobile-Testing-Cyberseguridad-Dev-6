package ar.edu.ispc.edutools;

import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import ar.edu.ispc.edutools.screens.ContactoScreen;

/**
 * AUT-UI-02 · Flujo critico: Contacto con correo mal formado (Issue #24).
 */
@RunWith(AndroidJUnit4.class)
public class ContactoFlujoTest {

    @BeforeClass
    public static void activarChequeosDeAccesibilidad() {
        AccessibilityChecks.enable().setRunChecksFromRootView(true);
    }

    @Rule
    public ActivityScenarioRule<ContactoActivity> regla =
            new ActivityScenarioRule<>(ContactoActivity.class);

    @Test
    public void contactoConEmailMalFormado_marcaErrorSoloEnCorreo() {
        // Arrange
        ContactoScreen contacto = new ContactoScreen()
                .completar("Juan Perez", "juan@", "Consulta", "Hola");
        // Act
        contacto.tocarEnviar();
        // Assert
        contacto.verificarErrorCorreo("Por favor ingresá un correo electrónico válido.")
                .verificarSinErrorEnNombreAsuntoYMensaje();
    }

    /** HUM-08, TC-21: validar una consulta no cierra la pantalla ni borra el mensaje. */
    @Test
    public void contactoValido_conservaConsultaEnPantallaSinEnviar() {
        ContactoScreen contacto = new ContactoScreen()
                .completar("Juan Perez", "juan@example.com", "Consulta", "Hola");
        contacto.tocarEnviar();
        contacto.verificarSinErrorEnNombreAsuntoYMensaje()
                .verificarConsultaSinEnviar("Hola");
    }
}
