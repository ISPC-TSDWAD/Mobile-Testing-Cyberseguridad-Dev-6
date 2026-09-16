package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ar.edu.ispc.edutools.model.UsuarioItem;

/** Perfil de un usuario. Activity hija de UsuariosActivity. */
public class PerfilUsuarioActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        configurarToolbar(true);

        TextView tvNombre = findViewById(R.id.tvPerfilNombre);
        TextView tvUsername = findViewById(R.id.tvPerfilUsername);
        TextView tvCorreo = findViewById(R.id.tvPerfilCorreo);
        TextView tvRol = findViewById(R.id.tvPerfilRol);
        TextView tvEstado = findViewById(R.id.tvPerfilEstado);

        Intent origen = getIntent();
        UsuarioItem usuario = null;
        if (origen != null && origen.hasExtra(UsuariosActivity.EXTRA_USUARIO_ITEM)) {
            usuario = (UsuarioItem) origen.getSerializableExtra(UsuariosActivity.EXTRA_USUARIO_ITEM);
        }

        if (usuario != null) {
            setTitle(usuario.getNombreCompleto());
            tvNombre.setText(usuario.getNombreCompleto());
            tvUsername.setText(usuario.getUsername());
            tvCorreo.setText(usuario.getCorreo());
            tvRol.setText(usuario.getRolLegible());
            tvEstado.setText(usuario.getEstadoLegible());
        }
    }
}
