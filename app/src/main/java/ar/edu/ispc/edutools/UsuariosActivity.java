package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ar.edu.ispc.edutools.adapter.UsuarioAdapter;
import ar.edu.ispc.edutools.model.DatosDemo;
import ar.edu.ispc.edutools.model.UsuarioItem;

/** Listado de usuarios del sistema. Activity hija de MainActivity. */
public class UsuariosActivity extends BaseActivity implements UsuarioAdapter.OnUsuarioClickListener {

    public static final String EXTRA_USUARIO_ITEM = "ar.edu.ispc.edutools.EXTRA_USUARIO_ITEM";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        configurarToolbar(true);

        List<UsuarioItem> usuarios = DatosDemo.listarUsuarios();

        TextView tvCantidad = findViewById(R.id.tvCantidadUsuarios);
        tvCantidad.setText(getString(R.string.usuarios_cantidad, usuarios.size()));

        RecyclerView rv = findViewById(R.id.rvUsuarios);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(new UsuarioAdapter(usuarios, this));
    }

    @Override
    public void onUsuarioClick(UsuarioItem usuario) {
        Intent intent = new Intent(this, PerfilUsuarioActivity.class);
        intent.putExtra(EXTRA_USUARIO_ITEM, usuario);
        startActivity(intent);
    }
}
