package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ar.edu.ispc.edutools.model.DatosDemo;
import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.ApiErrorMapper;
import ar.edu.ispc.edutools.net.RecursoApi;
import ar.edu.ispc.edutools.security.SessionManager;
import ar.edu.ispc.edutools.util.Validador;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Formulario de alta y edición de recursos pedagógicos.
 * Activity hija de RecursosActivity. Valida los datos con Validador y
 * envía POST para crear o PUT para editar.
 */
public class RecursoFormActivity extends BaseActivity {

    private static final String[] TIPOS_SLUGS = {
            "video", "acordeon", "infografia", "cuestionario", "lectura", "actividad"
    };

    private Recurso recursoExistente;
    private RecursoApi api;
    private SessionManager sessionManager;

    private TextInputLayout tilTitulo;
    private TextInputLayout tilUrl;
    private TextInputEditText etTitulo;
    private TextInputEditText etDescripcion;
    private TextInputEditText etUrl;
    private Spinner spTipo;
    private Spinner spCategoria;
    private Spinner spAsignatura;
    private Button btnGuardar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurso_form);
        configurarToolbar(true);

        api = ApiClient.create(this, RecursoApi.class);
        sessionManager = new SessionManager(this);

        tilTitulo = findViewById(R.id.tilFormTitulo);
        tilUrl = findViewById(R.id.tilFormUrl);
        etTitulo = findViewById(R.id.etFormTitulo);
        etDescripcion = findViewById(R.id.etFormDescripcion);
        etUrl = findViewById(R.id.etFormUrl);
        spTipo = findViewById(R.id.spTipo);
        spCategoria = findViewById(R.id.spCategoria);
        spAsignatura = findViewById(R.id.spAsignatura);
        btnGuardar = findViewById(R.id.btnGuardarRecurso);
        Button btnCancelar = findViewById(R.id.btnCancelarRecurso);

        cargarSpinner(spTipo, DatosDemo.tiposDeRecurso());
        cargarSpinner(spCategoria, DatosDemo.categorias());
        cargarSpinner(spAsignatura, DatosDemo.asignaturas());

        // Modo edición si se pasa un recurso en el Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RecursosActivity.EXTRA_RECURSO)) {
            recursoExistente = (Recurso) intent.getSerializableExtra(RecursosActivity.EXTRA_RECURSO);
            setTitle(R.string.recurso_editar);
            precargarDatos(recursoExistente);
        } else {
            setTitle(R.string.title_recurso_form);
        }

        btnGuardar.setOnClickListener(v -> guardarRecurso());
        btnCancelar.setOnClickListener(v -> finish());
    }

    private void precargarDatos(Recurso r) {
        if (r == null) {
            return;
        }
        if (r.getTitulo() != null) {
            etTitulo.setText(r.getTitulo());
        }
        if (r.getDescripcion() != null) {
            etDescripcion.setText(r.getDescripcion());
        }
        if (r.getUrl() != null) {
            etUrl.setText(r.getUrl());
        }

        // Seleccionar tipo
        for (int i = 0; i < TIPOS_SLUGS.length; i++) {
            if (TIPOS_SLUGS[i].equalsIgnoreCase(r.getTipo())) {
                spTipo.setSelection(i);
                break;
            }
        }

        // Categoría y Asignatura (1-indexados)
        if (r.getCategoria() > 0 && r.getCategoria() <= spCategoria.getCount()) {
            spCategoria.setSelection(r.getCategoria() - 1);
        }
        if (r.getAsignatura() > 0 && r.getAsignatura() <= spAsignatura.getCount()) {
            spAsignatura.setSelection(r.getAsignatura() - 1);
        }
    }

    private void guardarRecurso() {
        String titulo = texto(etTitulo);
        String descripcion = texto(etDescripcion);
        String url = texto(etUrl);

        boolean valido = true;

        if (!Validador.esTituloValido(titulo)) {
            tilTitulo.setError(getString(R.string.error_titulo_invalido));
            valido = false;
        } else {
            tilTitulo.setError(null);
        }

        if (!Validador.esUrlValida(url)) {
            tilUrl.setError(getString(R.string.error_url_invalida));
            valido = false;
        } else {
            tilUrl.setError(null);
        }

        if (!valido) {
            return;
        }

        int tipoPos = spTipo.getSelectedItemPosition();
        String tipo = tipoPos >= 0 && tipoPos < TIPOS_SLUGS.length ? TIPOS_SLUGS[tipoPos] : "video";
        int categoriaId = spCategoria.getSelectedItemPosition() + 1;
        int asignaturaId = spAsignatura.getSelectedItemPosition() + 1;

        btnGuardar.setEnabled(false);

        if (recursoExistente != null) {
            // Edición: PUT api/pedagogia/recursos/{id}/
            Recurso recursoAEnviar = new Recurso(recursoExistente.getId(), titulo.trim(),
                    descripcion.trim(), tipo, categoriaId, asignaturaId, url.trim());

            api.actualizar(recursoExistente.getId(), recursoAEnviar).enqueue(new ApiCallback<Recurso>() {
                @Override
                public void onResponse(Call<Recurso> call, Response<Recurso> respuesta) {
                    if (respuesta.code() == 401) {
                        manejarSesionVencida();
                        return;
                    }
                    super.onResponse(call, respuesta);
                }

                @Override
                public void onExito(Recurso cuerpo) {
                    Toast.makeText(RecursoFormActivity.this,
                            R.string.recurso_actualizado, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String mensaje) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(RecursoFormActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            });
        } else {
            // Creación: POST api/pedagogia/recursos/
            Recurso nuevoRecurso = new Recurso(titulo.trim(), descripcion.trim(),
                    tipo, categoriaId, asignaturaId, url.trim());

            api.crear(nuevoRecurso).enqueue(new ApiCallback<Recurso>() {
                @Override
                public void onResponse(Call<Recurso> call, Response<Recurso> respuesta) {
                    if (respuesta.code() == 401) {
                        manejarSesionVencida();
                        return;
                    }
                    super.onResponse(call, respuesta);
                }

                @Override
                public void onExito(Recurso cuerpo) {
                    Toast.makeText(RecursoFormActivity.this,
                            R.string.recurso_creado, Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onError(String mensaje) {
                    btnGuardar.setEnabled(true);
                    Toast.makeText(RecursoFormActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void manejarSesionVencida() {
        sessionManager.cerrarSesion();
        Toast.makeText(this, ApiErrorMapper.mensaje(401, null), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void cargarSpinner(Spinner spinner, String[] opciones) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
