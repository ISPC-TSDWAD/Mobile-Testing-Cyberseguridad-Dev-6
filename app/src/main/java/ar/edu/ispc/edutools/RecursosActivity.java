package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import ar.edu.ispc.edutools.adapter.RecursoAdapter;
import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.ApiErrorMapper;
import ar.edu.ispc.edutools.net.RecursoApi;
import ar.edu.ispc.edutools.security.SessionManager;
import retrofit2.Call;
import retrofit2.Response;

/** Listado de recursos pedagógicos. Consume GET api/pedagogia/recursos/. */
public class RecursosActivity extends BaseActivity implements RecursoAdapter.OnRecursoClickListener {

    /** Clave del extra con el recurso seleccionado. */
    public static final String EXTRA_RECURSO = "ar.edu.ispc.edutools.EXTRA_RECURSO";

    private final List<Recurso> recursos = new ArrayList<>();
    private RecursoAdapter adapter;
    private TextView tvCantidad;
    private TextView tvEstado;
    private ProgressBar progressBar;
    private RecyclerView rvRecursos;
    private RecursoApi api;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recursos);
        configurarToolbar(true);    // Activity hija: flecha de retroceso visible.

        api = ApiClient.create(this, RecursoApi.class);
        sessionManager = new SessionManager(this);

        tvCantidad = findViewById(R.id.tvCantidadRecursos);
        tvEstado = findViewById(R.id.tvEstadoRecursos);
        progressBar = findViewById(R.id.pbRecursos);
        rvRecursos = findViewById(R.id.rvRecursos);

        rvRecursos.setLayoutManager(new LinearLayoutManager(this));
        rvRecursos.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter = new RecursoAdapter(recursos, this);
        rvRecursos.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fabNuevoRecurso);
        fab.setOnClickListener(v ->
                startActivity(new Intent(RecursosActivity.this, RecursoFormActivity.class)));
    }

    /** Se recarga al volver a la pantalla tras crear, editar o eliminar un recurso. */
    @Override
    protected void onResume() {
        super.onResume();
        cargarRecursos();
    }

    private void cargarRecursos() {
        progressBar.setVisibility(View.VISIBLE);
        tvEstado.setVisibility(View.GONE);
        tvCantidad.setText(R.string.recursos_cargando);

        api.listar().enqueue(new ApiCallback<List<Recurso>>() {
            @Override
            public void onResponse(Call<List<Recurso>> call, Response<List<Recurso>> respuesta) {
                if (respuesta.code() == 401) {
                    manejarSesionVencida();
                    return;
                }
                super.onResponse(call, respuesta);
            }

            @Override
            public void onExito(List<Recurso> lista) {
                if (isFinishing()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                recursos.clear();
                if (lista != null) {
                    recursos.addAll(lista);
                }
                adapter.notifyDataSetChanged();

                if (recursos.isEmpty()) {
                    tvCantidad.setText(getString(R.string.recursos_cantidad, 0));
                    tvEstado.setText(R.string.recursos_vacio);
                    tvEstado.setVisibility(View.VISIBLE);
                    rvRecursos.setVisibility(View.GONE);
                } else {
                    tvCantidad.setText(getString(R.string.recursos_cantidad, recursos.size()));
                    tvEstado.setVisibility(View.GONE);
                    rvRecursos.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(String mensaje) {
                if (isFinishing()) {
                    return;
                }
                progressBar.setVisibility(View.GONE);
                recursos.clear();
                adapter.notifyDataSetChanged();
                tvCantidad.setText("");
                tvEstado.setText(mensaje);
                tvEstado.setVisibility(View.VISIBLE);
                rvRecursos.setVisibility(View.GONE);
            }
        });
    }

    private void manejarSesionVencida() {
        sessionManager.cerrarSesion();
        Toast.makeText(this, ApiErrorMapper.mensaje(401, null), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /** Envia el recurso serializado en el Intent hacia la pantalla de detalle. */
    @Override
    public void onRecursoClick(Recurso recurso) {
        Intent intent = new Intent(this, RecursoDetalleActivity.class);
        intent.putExtra(EXTRA_RECURSO, recurso);
        startActivity(intent);
    }
}
