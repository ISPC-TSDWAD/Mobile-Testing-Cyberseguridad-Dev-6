package ar.edu.ispc.edutools;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/** Matchers de Espresso para el error que muestra un TextInputLayout. */
public final class ErrorEnCampo {

    private ErrorEnCampo() { }

    /** El campo muestra exactamente este mensaje de error. */
    public static Matcher<View> conError(@NonNull String esperado) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if (!(view instanceof TextInputLayout)) {
                    return false;
                }
                CharSequence error = ((TextInputLayout) view).getError();
                return error != null && esperado.contentEquals(error);
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("TextInputLayout con error: " + esperado);
            }
        };
    }

    /** El campo no muestra ningun error. */
    public static Matcher<View> sinError() {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                return view instanceof TextInputLayout
                        && ((TextInputLayout) view).getError() == null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("TextInputLayout sin error");
            }
        };
    }
}
