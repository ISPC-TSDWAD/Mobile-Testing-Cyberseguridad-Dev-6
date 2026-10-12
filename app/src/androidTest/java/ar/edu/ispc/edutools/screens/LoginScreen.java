package ar.edu.ispc.edutools.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ar.edu.ispc.edutools.ErrorEnCampo.conError;
import static ar.edu.ispc.edutools.ErrorEnCampo.sinError;

import ar.edu.ispc.edutools.R;

/** Screen Object de LoginActivity: los tests no conocen los ids, solo acciones. */
public class LoginScreen {

    public LoginScreen escribirUsuario(String usuario) {
        onView(withId(R.id.etUsuario)).perform(typeText(usuario), closeSoftKeyboard());
        return this;
    }

    public LoginScreen tocarIngresar() {
        onView(withId(R.id.btnIngresar)).perform(click());
        return this;
    }

    public LoginScreen verificarErrorUsuario(String mensaje) {
        onView(withId(R.id.tilUsuario)).check(matches(conError(mensaje)));
        return this;
    }

    public LoginScreen verificarUsuarioSinError() {
        onView(withId(R.id.tilUsuario)).check(matches(sinError()));
        return this;
    }

    public LoginScreen verificarErrorPassword(String mensaje) {
        onView(withId(R.id.tilPassword)).check(matches(conError(mensaje)));
        return this;
    }

    public LoginScreen verificarSigueEnLogin() {
        onView(withId(R.id.btnIngresar)).check(matches(isDisplayed()));
        return this;
    }
}
