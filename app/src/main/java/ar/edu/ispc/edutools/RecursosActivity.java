package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import ar.edu.ispc.edutools.adapter.RecursoAdapter;
import ar.edu.ispc.edutools.model.DatosDemo;
import ar.edu.ispc.edutools.model.Recurso;

/** Listado de recursos pedagogicos. Activity hija de MainActivity. */
public class RecursosActivity extends BaseActivity implements RecursoAdapter.OnRecursoClickListener {

    /** Clave del extra con el recurso seleccionado. */
    public static final String EXTRA_RECURSO = "ar.edu.ispc.edutools.EXTRA_RECURSO";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recursos);
        configurarToolbar(true);    // Activity hija: flecha de retroceso visible.

        List<Recurso> recursos = DatosDemo.listarRecursos();

        TextView tvCantidad = findViewById(R.id.tvCantidadRecursos);
        tvCantidad.setText(getString(R.string.recursos_cantidad, recursos.size()));

        RecyclerView rv = findViewById(R.id.rvRecursos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rv.setAdapter(new RecursoAdapter(recursos, this));

        FloatingActionButton fab = findViewById(R.id.fabNuevoRecurso);
        fab.setOnClickListener(v ->
                startActivity(new Intent(RecursosActivity.this, RecursoFormActivity.class)));
    }

    /** Envia el recurso serializado en el Intent hacia la pantalla de detalle. */
    @Override
    public void onRecursoClick(Recurso recurso) {
        Intent intent = new Intent(this, RecursoDetalleActivity.class);
        intent.putExtra(EXTRA_RECURSO, recurso);
        startActivity(intent);
    }
}
