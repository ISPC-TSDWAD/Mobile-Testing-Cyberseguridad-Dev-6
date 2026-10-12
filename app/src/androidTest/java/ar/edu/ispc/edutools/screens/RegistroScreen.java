package ar.edu.ispc.edutools.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ar.edu.ispc.edutools.ErrorEnCampo.conError;
import static ar.edu.ispc.edutools.ErrorEnCampo.sinError;

import ar.edu.ispc.edutools.R;

/** Screen Object de RegistroActivity. */
public class RegistroScreen {

    public RegistroScreen completar(String username, String nombre, String apellido,
                                    String email, String password, String repeticion) {
        escribir(R.id.etRegUsername, username);
        escribir(R.id.etRegNombre, nombre);
        escribir(R.id.etRegApellido, apellido);
        escribir(R.id.etRegEmail, email);
        escribir(R.id.etRegPassword, password);
        escribir(R.id.etRegPassword2, repeticion);
        return this;
    }

    public RegistroScreen tocarRegistrar() {
        onView(withId(R.id.btnRegistrar)).perform(scrollTo(), click());
        return this;
    }

    public RegistroScreen verificarErrorRepeticion(String mensaje) {
        onView(withId(R.id.tilRegPassword2)).check(matches(conError(mensaje)));
        return this;
    }

    public RegistroScreen verificarSinErrorEnUsuarioYEmail() {
        onView(withId(R.id.tilRegUsername)).check(matches(sinError()));
        onView(withId(R.id.tilRegEmail)).check(matches(sinError()));
        return this;
    }

    public RegistroScreen verificarSigueEnRegistro() {
        onView(withId(R.id.btnRegistrar)).check(matches(isDisplayed()));
        return this;
    }

    private void escribir(int id, String texto) {
        onView(withId(id)).perform(scrollTo(), typeText(texto), closeSoftKeyboard());
    }
}
