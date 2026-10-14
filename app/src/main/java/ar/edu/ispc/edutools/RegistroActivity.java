package ar.edu.ispc.edutools;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.NuevoUsuarioRequest;
import ar.edu.ispc.edutools.net.UsuarioApi;
import ar.edu.ispc.edutools.net.UsuarioDto;
import ar.edu.ispc.edutools.util.Validador;

/**
 * Pantalla de Registro de usuario (Issue #23).
 * El boton Registrar valida los campos (Issue #24) y envia los datos
 * a POST api/users/ (solo admin, Issue #25).
 */
public class RegistroActivity extends BaseActivity {

    private TextInputLayout tilUsername, tilNombre, tilApellido, tilEmail, tilPassword, tilPassword2;
    private TextInputEditText etUsername, etNombre, etApellido, etEmail, etPassword, etPassword2;
    private Button btnRegistrar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        configurarToolbar(true);

        tilUsername = findViewById(R.id.tilRegUsername);
        tilNombre = findViewById(R.id.tilRegNombre);
        tilApellido = findViewById(R.id.tilRegApellido);
        tilEmail = findViewById(R.id.tilRegEmail);
        tilPassword = findViewById(R.id.tilRegPassword);
        tilPassword2 = findViewById(R.id.tilRegPassword2);

        etUsername = findViewById(R.id.etRegUsername);
        etNombre = findViewById(R.id.etRegNombre);
        etApellido = findViewById(R.id.etRegApellido);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etPassword2 = findViewById(R.id.etRegPassword2);

        btnRegistrar = findViewById(R.id.btnRegistrar);
        Button btnCancelar = findViewById(R.id.btnCancelarRegistro);

        btnRegistrar.setOnClickListener(v -> registrar());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void registrar() {
        String username = texto(etUsername).trim();
        if (!validar()) {
            return;
        }

        NuevoUsuarioRequest pedido = new NuevoUsuarioRequest(username, texto(etPassword),
                texto(etEmail).trim(), texto(etNombre).trim(), texto(etApellido).trim());

        btnRegistrar.setEnabled(false);   // evita enviar dos veces
        ApiClient.create(this, UsuarioApi.class).crear(pedido).enqueue(new ApiCallback<UsuarioDto>() {
            @Override
            public void onExito(UsuarioDto creado) {
                Toast.makeText(RegistroActivity.this,
                        getString(R.string.registro_exito, username), Toast.LENGTH_LONG).show();
                finish();
            }

            @Override
            public void onError(String mensaje) {
                btnRegistrar.setEnabled(true);
                Toast.makeText(RegistroActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    /** Marca el error en cada campo con problema. Devuelve true si todo esta bien. */
    private boolean validar() {
        String password = texto(etPassword);
        boolean ok = marcar(tilUsername, Validador.esUsernameValido(texto(etUsername)),
                R.string.registro_error_username);
        ok &= marcar(tilNombre, Validador.esObligatorioCompleto(texto(etNombre)),
                R.string.error_obligatorio);
        ok &= marcar(tilApellido, Validador.esObligatorioCompleto(texto(etApellido)),
                R.string.error_obligatorio);
        ok &= marcar(tilEmail, Validador.esEmailValido(texto(etEmail)),
                R.string.contacto_error_email);
        // Contrasena robusta: el mensaje dice que le falta (null si esta bien)
        String falta = Validador.mensajePassword(password);
        tilPassword.setError(falta);
        ok &= falta == null;
        ok &= marcar(tilPassword2, Validador.coincidenPasswords(password, texto(etPassword2)),
                R.string.registro_error_password2);
        return ok;
    }

    private boolean marcar(TextInputLayout campo, boolean valido, int idMensaje) {
        campo.setError(valido ? null : getString(idMensaje));
        return valido;
    }

    private static String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
