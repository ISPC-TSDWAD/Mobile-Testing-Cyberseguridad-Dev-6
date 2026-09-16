package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import ar.edu.ispc.edutools.model.Recurso;

/**
 * Detalle de un recurso pedagogico. Activity hija de RecursosActivity.
 * Recibe el objeto Recurso serializado en el Intent.
 */
public class RecursoDetalleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_detalle);
        configurarToolbar(true);

        TextView tvTitulo = findViewById(R.id.tvDetalleTitulo);
        TextView tvTipo = findViewById(R.id.tvDetalleTipo);
        TextView tvCategoria = findViewById(R.id.tvDetalleCategoria);
        TextView tvAsignatura = findViewById(R.id.tvDetalleAsignatura);
        TextView tvDescripcion = findViewById(R.id.tvDetalleDescripcion);
        TextView tvAutor = findViewById(R.id.tvDetalleAutor);
        Button btnEditar = findViewById(R.id.btnEditarRecurso);

        // Objeto enviado por RecursosActivity.
        Intent origen = getIntent();
        final Recurso recurso;
        if (origen != null && origen.hasExtra(RecursosActivity.EXTRA_RECURSO)) {
            recurso = (Recurso) origen.getSerializableExtra(RecursosActivity.EXTRA_RECURSO);
        } else {
            recurso = null;
        }

        if (recurso != null) {
            setTitle(recurso.getTitulo());
            tvTitulo.setText(recurso.getTitulo());
            tvTipo.setText(recurso.getTipoLegible());
            tvCategoria.setText(recurso.getCategoria());
            tvAsignatura.setText(recurso.getAsignatura());
            tvDescripcion.setText(recurso.getDescripcion());
            tvAutor.setText(recurso.getCreadoPor());
        }

        // El recurso se reenvia al formulario para precargar los campos.
        btnEditar.setOnClickListener(v -> {
            Intent intent = new Intent(RecursoDetalleActivity.this, RecursoFormActivity.class);
            if (recurso != null) {
                intent.putExtra(RecursosActivity.EXTRA_RECURSO, recurso);
            }
            startActivity(intent);
        });
    }
}
