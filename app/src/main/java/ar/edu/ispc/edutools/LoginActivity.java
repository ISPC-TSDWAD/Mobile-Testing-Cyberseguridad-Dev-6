package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

/**
 * Pantalla de acceso. Raiz de la navegacion de la aplicacion.
 * El nombre ingresado viaja a MainActivity como extra del Intent.
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
        Button btnIngresar = findViewById(R.id.btnIngresar);
        Button btnAcerca = findViewById(R.id.btnAcercaDesdeLogin);

        btnIngresar.setOnClickListener(v -> {
            String usuario = etUsuario.getText().toString().trim();
            if (usuario.isEmpty()) {
                usuario = "invitado";
            }

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra(EXTRA_USUARIO, usuario);
            startActivity(intent);
            // El login no debe quedar en la pila: al presionar atras desde el
            // Home, la aplicacion se cierra en lugar de volver al formulario.
            finish();
        });

        btnAcerca.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, AcercaDeActivity.class)));
    }
}
