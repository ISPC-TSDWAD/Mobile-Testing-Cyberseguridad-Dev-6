package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.RecursoApi;

/**
 * Detalle de un recurso pedagógico. Activity hija de RecursosActivity.
 * Recibe el objeto Recurso serializado en el Intent y se mantiene sincronizada
 * con el backend al volver de editar.
 */
public class RecursoDetalleActivity extends BaseActivity {

    private Recurso recurso;
    private RecursoApi api;

    private TextView tvTitulo;
    private TextView tvTipo;
    private TextView tvCategoria;
    private TextView tvAsignatura;
    private TextView tvDescripcion;
    private TextView tvAutor;

    private final ActivityResultLauncher<Intent> launcherEditar =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Recurso actualizado = (Recurso) result.getData()
                            .getSerializableExtra(RecursosActivity.EXTRA_RECURSO);
                    if (actualizado != null) {
                        recurso = actualizado;
                        mostrarDatos();
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_detalle);
        configurarToolbar(true);

        api = ApiClient.create(this, RecursoApi.class);

        tvTitulo = findViewById(R.id.tvDetalleTitulo);
        tvTipo = findViewById(R.id.tvDetalleTipo);
        tvCategoria = findViewById(R.id.tvDetalleCategoria);
        tvAsignatura = findViewById(R.id.tvDetalleAsignatura);
        tvDescripcion = findViewById(R.id.tvDetalleDescripcion);
        tvAutor = findViewById(R.id.tvDetalleAutor);
        Button btnEditar = findViewById(R.id.btnEditarRecurso);
        Button btnEliminar = findViewById(R.id.btnEliminarRecurso);

        // Objeto enviado por RecursosActivity.
        Intent origen = getIntent();
        if (origen != null && origen.hasExtra(RecursosActivity.EXTRA_RECURSO)) {
            recurso = (Recurso) origen.getSerializableExtra(RecursosActivity.EXTRA_RECURSO);
        }

        if (recurso != null) {
            mostrarDatos();

            btnEditar.setOnClickListener(v -> {
                Intent intent = new Intent(RecursoDetalleActivity.this, RecursoFormActivity.class);
                intent.putExtra(RecursosActivity.EXTRA_RECURSO, recurso);
                launcherEditar.launch(intent);
            });

            btnEliminar.setOnClickListener(v -> confirmarEliminar());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recurso != null) {
            cargarDetalleDesdeApi(recurso.getId());
        }
    }

    private void mostrarDatos() {
        if (recurso == null) {
            return;
        }
        setTitle(recurso.getTitulo());
        tvTitulo.setText(recurso.getTitulo());
        tvTipo.setText(recurso.getTipoLegible());
        tvCategoria.setText(recurso.getCategoriaNombre());
        tvAsignatura.setText(recurso.getAsignaturaNombre());
        tvDescripcion.setText(recurso.getDescripcion());
        tvAutor.setText(recurso.getCreadoPor() != null ? String.valueOf(recurso.getCreadoPor()) : "");
    }

    private void cargarDetalleDesdeApi(int id) {
        api.detalle(id).enqueue(new CallbackConSesion<Recurso>() {

            @Override
            public void onExito(Recurso cuerpo) {
                if (isFinishing() || cuerpo == null) {
                    return;
                }
                recurso = cuerpo;
                mostrarDatos();
            }

            @Override
            public void onError(String mensaje) {
                // Si falla la red, se mantienen los datos cargados previamente
            }
        });
    }

    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.recurso_eliminar_confirmar, recurso.getTitulo()))
                .setPositiveButton(R.string.recurso_eliminar, (dialog, which) -> eliminar())
                .setNegativeButton(R.string.form_cancelar, null)
                .show();
    }

    private void eliminar() {
        api.eliminar(recurso.getId()).enqueue(new CallbackConSesion<Void>() {

            @Override
            public void onExito(Void cuerpo) {
                Toast.makeText(RecursoDetalleActivity.this,
                        R.string.recurso_eliminado, Toast.LENGTH_SHORT).show();
                finish(); // vuelve al listado, que se recarga solo en onResume()
            }

            @Override
            public void onError(String mensaje) {
                Toast.makeText(RecursoDetalleActivity.this, mensaje, Toast.LENGTH_LONG).show();
            }
        });
    }

}
