package ar.edu.ispc.edutools.screens;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static ar.edu.ispc.edutools.ErrorEnCampo.conError;
import static ar.edu.ispc.edutools.ErrorEnCampo.sinError;

import ar.edu.ispc.edutools.R;

/** Screen Object de ContactoActivity. */
public class ContactoScreen {

    public ContactoScreen completar(String nombre, String correo, String asunto, String mensaje) {
        escribir(R.id.etContactoNombre, nombre);
        escribir(R.id.etContactoCorreo, correo);
        escribir(R.id.etContactoAsunto, asunto);
        escribir(R.id.etContactoMensaje, mensaje);
        return this;
    }

    public ContactoScreen tocarEnviar() {
        onView(withId(R.id.btnEnviarConsulta)).perform(scrollTo(), click());
        return this;
    }

    public ContactoScreen verificarErrorCorreo(String mensaje) {
        onView(withId(R.id.tilContactoCorreo)).check(matches(conError(mensaje)));
        return this;
    }

    public ContactoScreen verificarSinErrorEnNombreAsuntoYMensaje() {
        onView(withId(R.id.tilContactoNombre)).check(matches(sinError()));
        onView(withId(R.id.tilContactoAsunto)).check(matches(sinError()));
        onView(withId(R.id.tilContactoMensaje)).check(matches(sinError()));
        return this;
    }

    private void escribir(int id, String texto) {
        onView(withId(id)).perform(scrollTo(), typeText(texto), closeSoftKeyboard());
    }
}
