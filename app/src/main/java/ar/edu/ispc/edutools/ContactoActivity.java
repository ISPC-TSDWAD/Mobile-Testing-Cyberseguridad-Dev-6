package ar.edu.ispc.edutools;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import ar.edu.ispc.edutools.util.Validador;

/**
 * Pantalla de contacto con la mesa de ayuda de EduTools.
 * Activity hija de MainActivity. Validaciones: Issue #24.
 */
public class ContactoActivity extends BaseActivity {

    private EditText etNombre, etCorreo, etAsunto, etMensaje;
    private TextInputLayout tilNombre, tilCorreo, tilAsunto, tilMensaje;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        configurarToolbar(true);

        etNombre = findViewById(R.id.etContactoNombre);
        etCorreo = findViewById(R.id.etContactoCorreo);
        etAsunto = findViewById(R.id.etContactoAsunto);
        etMensaje = findViewById(R.id.etContactoMensaje);

        tilNombre = findViewById(R.id.tilContactoNombre);
        tilCorreo = findViewById(R.id.tilContactoCorreo);
        tilAsunto = findViewById(R.id.tilContactoAsunto);
        tilMensaje = findViewById(R.id.tilContactoMensaje);

        Button btnEnviar = findViewById(R.id.btnEnviarConsulta);
        btnEnviar.setOnClickListener(v -> procesarEnvio());
    }

    private void procesarEnvio() {
        // Cada error se muestra en su campo (TextInputLayout.setError) y TalkBack lo anuncia.
        boolean ok = marcar(tilNombre, Validador.esObligatorioCompleto(texto(etNombre)),
                R.string.error_obligatorio);
        ok &= marcar(tilCorreo, Validador.esEmailValido(texto(etCorreo)),
                R.string.contacto_error_email);
        ok &= marcar(tilAsunto, Validador.esObligatorioCompleto(texto(etAsunto)),
                R.string.error_obligatorio);
        ok &= marcar(tilMensaje, Validador.esObligatorioCompleto(texto(etMensaje)),
                R.string.error_obligatorio);
        if (!ok) {
            return;
        }

        Toast.makeText(this, getString(R.string.contacto_sin_envio), Toast.LENGTH_LONG).show();
    }

    private boolean marcar(TextInputLayout campo, boolean valido, int idMensaje) {
        campo.setError(valido ? null : getString(idMensaje));
        return valido;
    }

    private static String texto(EditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
