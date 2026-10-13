package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.ApiErrorMapper;
import ar.edu.ispc.edutools.net.RecursoApi;
import ar.edu.ispc.edutools.security.SessionManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Detalle de un recurso pedagógico. Activity hija de RecursosActivity.
 * Recibe el objeto Recurso serializado en el Intent.
 */
public class RecursoDetalleActivity extends BaseActivity {

    private Recurso recurso;
    private RecursoApi api;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_detalle);
        configurarToolbar(true);

        api = ApiClient.create(this, RecursoApi.class);
        sessionManager = new SessionManager(this);

        TextView tvTitulo = findViewById(R.id.tvDetalleTitulo);
        TextView tvTipo = findViewById(R.id.tvDetalleTipo);
        TextView tvCategoria = findViewById(R.id.tvDetalleCategoria);
        TextView tvAsignatura = findViewById(R.id.tvDetalleAsignatura);
        TextView tvDescripcion = findViewById(R.id.tvDetalleDescripcion);
        TextView tvAutor = findViewById(R.id.tvDetalleAutor);
        Button btnEditar = findViewById(R.id.btnEditarRecurso);
        Button btnEliminar = findViewById(R.id.btnEliminarRecurso);

        // Objeto enviado por RecursosActivity.
        Intent origen = getIntent();
        if (origen != null && origen.hasExtra(RecursosActivity.EXTRA_RECURSO)) {
            recurso = (Recurso) origen.getSerializableExtra(RecursosActivity.EXTRA_RECURSO);
        }

        if (recurso != null) {
            setTitle(recurso.getTitulo());
            tvTitulo.setText(recurso.getTitulo());
            tvTipo.setText(recurso.getTipoLegible());
            tvCategoria.setText(recurso.getCategoriaNombre());
            tvAsignatura.setText(recurso.getAsignaturaNombre());
            tvDescripcion.setText(recurso.getDescripcion());
            tvAutor.setText(recurso.getCreadoPor() != null ? String.valueOf(recurso.getCreadoPor()) : "");

            btnEditar.setOnClickListener(v -> {
                Intent intent = new Intent(RecursoDetalleActivity.this, RecursoFormActivity.class);
                intent.putExtra(RecursosActivity.EXTRA_RECURSO, recurso);
                startActivity(intent);
            });

            btnEliminar.setOnClickListener(v -> confirmarEliminar());
        }
    }

    private void confirmarEliminar() {
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.recurso_eliminar_confirmar, recurso.getTitulo()))
                .setPositiveButton(R.string.recurso_eliminar, (dialog, which) -> eliminar())
                .setNegativeButton(R.string.form_cancelar, null)
                .show();
    }

    private void eliminar() {
        api.eliminar(recurso.getId()).enqueue(new ApiCallback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> respuesta) {
                if (respuesta.code() == 401) {
                    manejarSesionVencida();
                    return;
                }
                super.onResponse(call, respuesta);
            }

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

    private void manejarSesionVencida() {
        sessionManager.cerrarSesion();
        Toast.makeText(this, ApiErrorMapper.mensaje(401, null), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
