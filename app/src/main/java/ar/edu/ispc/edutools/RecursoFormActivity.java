package ar.edu.ispc.edutools;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.Nullable;

import ar.edu.ispc.edutools.model.DatosDemo;

/**
 * Formulario de alta y edicion de recursos pedagogicos.
 * Activity hija de RecursosActivity.
 */
public class RecursoFormActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_form);
        configurarToolbar(true);

        // Cuando se llega desde el detalle, el recurso viaja en el Intent
        // y el formulario se presenta en modo edicion.
        if (getIntent() != null && getIntent().hasExtra(RecursosActivity.EXTRA_RECURSO)) {
            setTitle(R.string.recurso_editar);
        }

        cargarSpinner(R.id.spTipo, DatosDemo.tiposDeRecurso());
        cargarSpinner(R.id.spCategoria, DatosDemo.categorias());
        cargarSpinner(R.id.spAsignatura, DatosDemo.asignaturas());

        Button btnGuardar = findViewById(R.id.btnGuardarRecurso);
        Button btnCancelar = findViewById(R.id.btnCancelarRecurso);

        btnGuardar.setOnClickListener(v -> finish());
        btnCancelar.setOnClickListener(v -> finish());
    }

    /** Carga las opciones de un desplegable del formulario. */
    private void cargarSpinner(int idSpinner, String[] opciones) {
        Spinner spinner = findViewById(idSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }
}
