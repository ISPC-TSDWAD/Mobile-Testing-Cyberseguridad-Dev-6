package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ispc.edutools.adapter.UsuarioAdapter;
import ar.edu.ispc.edutools.model.UsuarioItem;
import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.UsuarioApi;
import ar.edu.ispc.edutools.net.UsuarioDto;
import ar.edu.ispc.edutools.security.SessionManager;
import ar.edu.ispc.edutools.util.JwtUtil;

/**
 * Listado de usuarios que viene de GET api/users/ (solo admin).
 * Si el rol no es admin, el servidor responde 403 y se muestra ese mensaje.
 */
public class UsuariosActivity extends BaseActivity implements UsuarioAdapter.OnUsuarioClickListener {

    public static final String EXTRA_USUARIO_ITEM = "ar.edu.ispc.edutools.EXTRA_USUARIO_ITEM";

    private final List<UsuarioItem> usuarios = new ArrayList<>();
    private UsuarioAdapter adapter;
    private TextView tvCantidad;
    private UsuarioApi api;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios);
        configurarToolbar(true);

        api = ApiClient.create(this, UsuarioApi.class);
        tvCantidad = findViewById(R.id.tvCantidadUsuarios);

        RecyclerView rv = findViewById(R.id.rvUsuarios);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new UsuarioAdapter(usuarios, this);
        rv.setAdapter(adapter);

        // El boton "+" solo lo ve el admin (el rol sale del token).
        boolean esAdmin = JwtUtil.esAdmin(new SessionManager(this).getAccessToken());
        View fab = findViewById(R.id.fabNuevoUsuario);
        fab.setVisibility(esAdmin ? View.VISIBLE : View.GONE);
        fab.setOnClickListener(v -> startActivity(new Intent(this, RegistroActivity.class)));
    }

    /** Se recarga cada vez que se vuelve: asi se ve un usuario recien creado, editado o eliminado. */
    @Override
    protected void onResume() {
        super.onResume();
        cargarUsuarios();
    }

    private void cargarUsuarios() {
        tvCantidad.setText(R.string.usuarios_cargando);
        api.listar().enqueue(new ApiCallback<List<UsuarioDto>>() {
            @Override
            public void onSesionVencida(String mensaje) {
                cerrarSesionPorVencimiento();
            }

            @Override
            public void onExito(List<UsuarioDto> lista) {
                if (isFinishing()) {
                    return;
                }
                usuarios.clear();
                if (lista != null) {
                    for (UsuarioDto dto : lista) {
                        usuarios.add(dto.toItem());
                    }
                }
                adapter.notifyDataSetChanged();
                tvCantidad.setText(getString(R.string.usuarios_cantidad, usuarios.size()));
            }

            @Override
            public void onError(String mensaje) {
                if (isFinishing()) {
                    return;
                }
                usuarios.clear();
                adapter.notifyDataSetChanged();
                tvCantidad.setText(mensaje);
            }
        });
    }

    @Override
    public void onUsuarioClick(UsuarioItem usuario) {
        Intent intent = new Intent(this, PerfilUsuarioActivity.class);
        intent.putExtra(EXTRA_USUARIO_ITEM, usuario);
        startActivity(intent);
    }
}
