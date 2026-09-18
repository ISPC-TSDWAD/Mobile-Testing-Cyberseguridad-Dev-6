package ar.edu.ispc.edutools;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;

/**
 * Pantalla de contacto con la mesa de ayuda de EduTools.
 * Activity hija de MainActivity.
 */
public class ContactoActivity extends BaseActivity {

    private EditText etNombre;
    private EditText etCorreo;
    private EditText etAsunto;
    private EditText etMensaje;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        configurarToolbar(true);

        etNombre = findViewById(R.id.etContactoNombre);
        etCorreo = findViewById(R.id.etContactoCorreo);
        etAsunto = findViewById(R.id.etContactoAsunto);
        etMensaje = findViewById(R.id.etContactoMensaje);

        Button btnEnviar = findViewById(R.id.btnEnviarConsulta);
        btnEnviar.setOnClickListener(v -> procesarEnvio());
    }

    private void procesarEnvio() {
        String nombre = etNombre != null ? etNombre.getText().toString().trim() : "";
        String correo = etCorreo != null ? etCorreo.getText().toString().trim() : "";
        String asunto = etAsunto != null ? etAsunto.getText().toString().trim() : "";
        String mensaje = etMensaje != null ? etMensaje.getText().toString().trim() : "";

        if (TextUtils.isEmpty(nombre) || TextUtils.isEmpty(correo)
                || TextUtils.isEmpty(asunto) || TextUtils.isEmpty(mensaje)) {
            Toast.makeText(this, getString(R.string.contacto_error_campos), Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            Toast.makeText(this, getString(R.string.contacto_error_email), Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, getString(R.string.contacto_exito), Toast.LENGTH_LONG).show();
        finish();
    }
}
