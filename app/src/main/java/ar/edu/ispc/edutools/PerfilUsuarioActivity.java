package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ar.edu.ispc.edutools.model.UsuarioItem;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.EditarUsuarioRequest;
import ar.edu.ispc.edutools.net.UsuarioApi;
import ar.edu.ispc.edutools.net.UsuarioDto;
import ar.edu.ispc.edutools.security.SessionManager;
import ar.edu.ispc.edutools.util.JwtUtil;
import ar.edu.ispc.edutools.util.Validador;

/**
 * Perfil de un usuario. Activity hija de UsuariosActivity.
 * El admin ve los botones Editar (PATCH api/users/{id}/) y Eliminar (DELETE).
 */
public class PerfilUsuarioActivity extends BaseActivity {

    private UsuarioItem usuario;
    private UsuarioApi api;

    private TextView tvNombre, tvUsername, tvCorreo, tvRol, tvEstado;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        configurarToolbar(true);

        api = ApiClient.create(this, UsuarioApi.class);

        tvNombre = findViewById(R.id.tvPerfilNombre);
        tvUsername = findViewById(R.id.tvPerfilUsername);
        tvCorreo = findViewById(R.id.tvPerfilCorreo);
        tvRol = findViewById(R.id.tvPerfilRol);
        tvEstado = findViewById(R.id.tvPerfilEstado);

        Intent origen = getIntent();
        if (origen != null && origen.hasExtra(UsuariosActivity.EXTRA_USUARIO_ITEM)) {
            usuario = (UsuarioItem) origen.getSerializableExtra(UsuariosActivity.EXTRA_USUARIO_ITEM);
        }
        mostrarDatos();

        // Editar y eliminar solo para admin (el rol sale del token).
        boolean esAdmin = JwtUtil.esAdmin(new SessionManager(this).getAccessToken());
        Button btnEditar = findViewById(R.id.btnEditarUsuario);
        Button btnEliminar = findViewById(R.id.btnEliminarUsuario);
        if (esAdmin && usuario != null) {
            btnEditar.setVisibility(View.VISIBLE);
            btnEliminar.setVisibility(View.VISIBLE);
            btnEditar.setOnClickListener(v -> mostrarDialogoEditar());
            btnEliminar.setOnClickListener(v -> confirmarEliminar());
        }
    }

    private void mostrarDatos() {
        if (usuario == null) {
            return;
        }
        // El titulo es fijo: el nombre ya se muestra abajo y asi no se repite el mismo texto (TalkBack).
        setTitle(R.string.perfil_titulo);
        if (usuario.getUsername() != null && usuario.getUsername().equals(usuario.getNombreCompleto())) {
            tvNombre.setText(R.string.perfil_sin_nombre);
        } else {
            tvNombre.setText(usuario.getNombreCompleto());
        }
        tvUsername.setText(usuario.getUsername());
        tvCorreo.setText(usuario.getCorreo());
        tvRol.setText(usuario.getRolLegible());
        tvEstado.setText(usuario.getEstadoLegible());
    }

    // ---------------- Editar ----------------

    private void mostrarDialogoEditar() {
        View vista = getLayoutInflater().inflate(R.layout.dialog_editar_usuario, null);
        final TextInputLayout tilNombre = vista.findViewById(R.id.tilEdNombre);
        final TextInputLayout tilApellido = vista.findViewById(R.id.tilEdApellido);
        final TextInputLayout tilEmail = vista.findViewById(R.id.tilEdEmail);
        final TextInputEditText etNombre = vista.findViewById(R.id.etEdNombre);
        final TextInputEditText etApellido = vista.findViewById(R.id.etEdApellido);
        final TextInputEditText etEmail = vista.findViewById(R.id.etEdEmail);

        etNombre.setText(usuario.getNombre());
        etApellido.setText(usuario.getApellido());
        etEmail.setText(usuario.getCorreo());

        final AlertDialog dialogo = new AlertDialog.Builder(this)
                .setTitle(R.string.usuario_editar_titulo)
                .setView(vista)
                .setPositiveButton(R.string.usuario_guardar, null)
                .setNegativeButton(R.string.form_cancelar, null)
                .create();
        dialogo.show();

        // Se asigna el click despues de show() para que el dialogo no se cierre si hay errores.
        dialogo.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String nombre = texto(etNombre);
            String apellido = texto(etApellido);
            String email = texto(etEmail);

            boolean ok = marcar(tilNombre, Validador.esObligatorioCompleto(nombre), R.string.error_obligatorio);
            ok &= marcar(tilApellido, Validador.esObligatorioCompleto(apellido), R.string.error_obligatorio);
            // El correo puede quedar vacio (el backend lo permite), pero si se escribe tiene que ser valido.
            ok &= marcar(tilEmail, email.trim().isEmpty() || Validador.esEmailValido(email),
                    R.string.contacto_error_email);
            if (!ok) {
                return;
            }

            api.editar(usuario.getId(), new EditarUsuarioRequest(nombre.trim(), apellido.trim(), email.trim()))
                    .enqueue(new CallbackConSesion<UsuarioDto>() {
                        @Override
                        public void onExito(UsuarioDto actualizado) {
                            if (actualizado != null) {
                                usuario = actualizado.toItem();
                                mostrarDatos();
                            }
                            dialogo.dismiss();
                            Toast.makeText(PerfilUsuarioActivity.this,
                                    R.string.usuario_actualizado, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(String mensaje) {
                            Toast.makeText(PerfilUsuarioActivity.this, mensaje, Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }

    // ---------------- Eliminar ----------------

    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.usuario_eliminar_confirmar, usuario.getNombreCompleto()))
                .setPositiveButton(R.string.usuario_eliminar, (d, cual) -> eliminar())
                .setNegativeButton(R.string.form_cancelar, null)
                .show();
    }

    private void eliminar() {
        api.eliminar(usuario.getId()).enqueue(new CallbackConSesion<Void>() {
            @Override
            public void onExito(Void cuerpo) {
                Toast.makeText(PerfilUsuarioActivity.this,
                        R.string.usuario_eliminado, Toast.LENGTH_SHORT).show();
                finish();   // vuelve al listado, que se recarga solo
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(PerfilUsuarioActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

    // ---------------- Auxiliares ----------------

    private boolean marcar(TextInputLayout campo, boolean valido, int idMensaje) {
        campo.setError(valido ? null : getString(idMensaje));
        return valido;
    }

    private static String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
