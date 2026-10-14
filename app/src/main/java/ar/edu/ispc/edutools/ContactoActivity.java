package ar.edu.ispc.edutools;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import ar.edu.ispc.edutools.util.ConsultaMail;
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

        // La app no tiene un endpoint de consultas: se arma el correo y lo envia la app de mail.
        Intent correo = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", ConsultaMail.DESTINO, null));
        correo.putExtra(Intent.EXTRA_SUBJECT, ConsultaMail.asunto(texto(etAsunto)));
        correo.putExtra(Intent.EXTRA_TEXT,
                ConsultaMail.cuerpo(texto(etNombre), texto(etCorreo), texto(etMensaje)));
        try {
            startActivity(correo);
            finish();
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.contacto_sin_app_correo, Toast.LENGTH_LONG).show();
        }
    }

    private boolean marcar(TextInputLayout campo, boolean valido, int idMensaje) {
        campo.setError(valido ? null : getString(idMensaje));
        return valido;
    }

    private static String texto(EditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
