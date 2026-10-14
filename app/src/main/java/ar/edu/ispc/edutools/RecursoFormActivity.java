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

import java.util.ArrayList;
import java.util.List;

import ar.edu.ispc.edutools.model.DatosDemo;
import ar.edu.ispc.edutools.model.ItemCatalogo;
import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.util.CatalogoUtil;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.ApiErrorMapper;
import ar.edu.ispc.edutools.net.RecursoApi;
import ar.edu.ispc.edutools.security.SessionManager;
import ar.edu.ispc.edutools.util.Validador;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Formulario de alta y edición de recursos pedagógicos.
 * Activity hija de RecursosActivity. Carga los catálogos reales de la API
 * (categorías y asignaturas), valida los datos con Validador y
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

    private final List<ItemCatalogo> categoriasList = new ArrayList<>();
    private final List<ItemCatalogo> asignaturasList = new ArrayList<>();
    private ArrayAdapter<ItemCatalogo> adapterCategorias;
    private ArrayAdapter<ItemCatalogo> adapterAsignaturas;

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

        cargarSpinnerStrings(spTipo, DatosDemo.tiposDeRecurso());

        adapterCategorias = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoriasList);
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(adapterCategorias);

        adapterAsignaturas = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, asignaturasList);
        adapterAsignaturas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAsignatura.setAdapter(adapterAsignaturas);

        // Modo edición si se pasa un recurso en el Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RecursosActivity.EXTRA_RECURSO)) {
            recursoExistente = (Recurso) intent.getSerializableExtra(RecursosActivity.EXTRA_RECURSO);
            setTitle(R.string.recurso_editar);
            precargarDatos(recursoExistente);
        } else {
            setTitle(R.string.title_recurso_form);
        }

        cargarCatalogosDesdeApi();

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

        // Si tenemos datos del recurso, poblamos temporalmente los adapters con la opción actual
        if (categoriasList.isEmpty() && r.getCategoria() > 0) {
            String nombreCat = r.getCategoriaNombre().isEmpty() ? "Categoría " + r.getCategoria() : r.getCategoriaNombre();
            categoriasList.add(new ItemCatalogo(r.getCategoria(), nombreCat));
            adapterCategorias.notifyDataSetChanged();
            spCategoria.setSelection(0);
        }

        if (asignaturasList.isEmpty() && r.getAsignatura() > 0) {
            String nombreAsig = r.getAsignaturaNombre().isEmpty() ? "Asignatura " + r.getAsignatura() : r.getAsignaturaNombre();
            asignaturasList.add(new ItemCatalogo(r.getAsignatura(), nombreAsig));
            adapterAsignaturas.notifyDataSetChanged();
            spAsignatura.setSelection(0);
        }
    }

    private void cargarCatalogosDesdeApi() {
        api.listarCategorias().enqueue(new ApiCallback<List<ItemCatalogo>>() {
            @Override
            public void onExito(List<ItemCatalogo> lista) {
                if (isFinishing() || lista == null || lista.isEmpty()) {
                    return;
                }
                categoriasList.clear();
                categoriasList.addAll(lista);
                adapterCategorias.notifyDataSetChanged();

                if (recursoExistente != null && recursoExistente.getCategoria() > 0) {
                    for (int i = 0; i < categoriasList.size(); i++) {
                        if (categoriasList.get(i).getId() == recursoExistente.getCategoria()) {
                            spCategoria.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onSesionVencida(String mensaje) {
                cerrarSesionPorVencimiento();
            }

            @Override
            public void onError(String mensaje) {
                if (!isFinishing()) {
                    Toast.makeText(RecursoFormActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            }
        });

        api.listarAsignaturas().enqueue(new ApiCallback<List<ItemCatalogo>>() {
            @Override
            public void onExito(List<ItemCatalogo> lista) {
                if (isFinishing() || lista == null || lista.isEmpty()) {
                    return;
                }
                asignaturasList.clear();
                asignaturasList.addAll(lista);
                adapterAsignaturas.notifyDataSetChanged();

                if (recursoExistente != null && recursoExistente.getAsignatura() > 0) {
                    for (int i = 0; i < asignaturasList.size(); i++) {
                        if (asignaturasList.get(i).getId() == recursoExistente.getAsignatura()) {
                            spAsignatura.setSelection(i);
                            break;
                        }
                    }
                }
            }

            @Override
            public void onSesionVencida(String mensaje) {
                cerrarSesionPorVencimiento();
            }

            @Override
            public void onError(String mensaje) {
                if (!isFinishing()) {
                    Toast.makeText(RecursoFormActivity.this, mensaje, Toast.LENGTH_LONG).show();
                }
            }
        });
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

        int categoriaId = CatalogoUtil.resolverId(spCategoria.getSelectedItem(),
                recursoExistente != null ? recursoExistente.getCategoria() : 0);
        int asignaturaId = CatalogoUtil.resolverId(spAsignatura.getSelectedItem(),
                recursoExistente != null ? recursoExistente.getAsignatura() : 0);
        if (categoriaId == CatalogoUtil.SIN_ID || asignaturaId == CatalogoUtil.SIN_ID) {
            // No se guarda con un valor inventado: primero tienen que cargar los catalogos.
            Toast.makeText(this, R.string.form_error_catalogos, Toast.LENGTH_LONG).show();
            cargarCatalogosDesdeApi();
            return;
        }

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
                    Intent data = new Intent();
                    data.putExtra(RecursosActivity.EXTRA_RECURSO, cuerpo != null ? cuerpo : recursoAEnviar);
                    setResult(RESULT_OK, data);
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
        cerrarSesionPorVencimiento();
    }

    private void cargarSpinnerStrings(Spinner spinner, String[] opciones) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, opciones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private static String texto(TextInputEditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString();
    }
}
