package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputLayout;

import ar.edu.ispc.edutools.util.Validador;

/**
 * Pantalla de acceso. Raiz de la navegacion de la aplicacion.
 * El nombre ingresado viaja a MainActivity como extra del Intent.
 * Validaciones del formulario: Issue #24. El login real con JWT: Issue #25.
 */
public class LoginActivity extends BaseActivity {

    /** Clave del extra con el nombre del usuario que inicio sesion. */
    public static final String EXTRA_USUARIO = "ar.edu.ispc.edutools.EXTRA_USUARIO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        configurarToolbar(false);   // Activity raiz: sin flecha de retroceso.

        final EditText etUsuario = findViewById(R.id.etUsuario);
        final EditText etPassword = findViewById(R.id.etPassword);
        final TextInputLayout tilUsuario = findViewById(R.id.tilUsuario);
        final TextInputLayout tilPassword = findViewById(R.id.tilPassword);
        Button btnIngresar = findViewById(R.id.btnIngresar);
        Button btnAcerca = findViewById(R.id.btnAcercaDesdeLogin);

        btnIngresar.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString();
            String password = etPassword.getText().toString();

            // El error se muestra en el propio campo y TalkBack lo anuncia.
            boolean usuarioOk = Validador.esObligatorioCompleto(usuario);
            boolean passwordOk = Validador.esObligatorioCompleto(password);
            tilUsuario.setError(usuarioOk ? null : getString(R.string.error_obligatorio));
            tilPassword.setError(passwordOk ? null : getString(R.string.error_obligatorio));
            if (!usuarioOk || !passwordOk) {
                return;
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_USUARIO, usuario.trim());
            startActivity(intent);
            // El login no debe quedar en la pila: al presionar atras desde el
            // Home, la aplicacion se cierra en lugar de volver al formulario.
            finish();
        });

        btnAcerca.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, AcercaDeActivity.class)));
    }
}
