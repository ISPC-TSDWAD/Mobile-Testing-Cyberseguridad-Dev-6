package ar.edu.ispc.edutools;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import java.io.ByteArrayInputStream;

import ar.edu.ispc.edutools.model.Recurso;
import ar.edu.ispc.edutools.net.ApiClient;
import ar.edu.ispc.edutools.net.RecursoApi;
import ar.edu.ispc.edutools.util.VistaPreviaRecurso;

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

    // Vista previa del contenido
    private View marcoContenido;
    private WebView webContenido;
    private TextView tvPreviewMensaje;
    private TextView tvPreviewAviso;
    private Button btnAbrirEnlace;
    /** Ultimo documento cargado, para no recargar el WebView si no cambio. */
    private String documentoCargado;

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
        marcoContenido = findViewById(R.id.marcoContenido);
        webContenido = findViewById(R.id.webContenido);
        tvPreviewMensaje = findViewById(R.id.tvPreviewMensaje);
        tvPreviewAviso = findViewById(R.id.tvPreviewAviso);
        btnAbrirEnlace = findViewById(R.id.btnAbrirEnlace);
        configurarWebView();
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
    protected void onDestroy() {
        if (webContenido != null) {
            webContenido.stopLoading();
            webContenido.destroy();
        }
        super.onDestroy();
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
        mostrarVistaPrevia();
    }

    /**
     * WebView restringido: sin JavaScript, sin acceso a archivos ni a contenido de la app,
     * sin contenido mixto y sin navegar dentro de la vista. Los enlaces se resuelven afuera.
     */
    @SuppressLint("ClickableViewAccessibility")
    @SuppressWarnings("deprecation")
    private void configurarWebView() {
        WebSettings ajustes = webContenido.getSettings();
        ajustes.setJavaScriptEnabled(false);
        ajustes.setAllowFileAccess(false);
        ajustes.setAllowContentAccess(false);
        ajustes.setAllowFileAccessFromFileURLs(false);
        ajustes.setAllowUniversalAccessFromFileURLs(false);
        ajustes.setDomStorageEnabled(false);
        ajustes.setDatabaseEnabled(false);
        ajustes.setGeolocationEnabled(false);
        ajustes.setSupportMultipleWindows(false);
        ajustes.setMixedContentMode(WebSettings.MIXED_CONTENT_NEVER_ALLOW);

        webContenido.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView vista, WebResourceRequest pedido) {
                manejarEnlace(pedido.getUrl().toString());
                return true;   // nunca se navega dentro del WebView
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView vista, String url) {
                manejarEnlace(url);
                return true;
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView vista, WebResourceRequest pedido) {
                // Contenido externo: solo imagenes por HTTPS (o embebidas). Lo demas se corta.
                String esquema = pedido.getUrl().getScheme();
                boolean permitido = "https".equalsIgnoreCase(esquema)
                        || "data".equalsIgnoreCase(esquema) || "about".equalsIgnoreCase(esquema);
                if (permitido) {
                    return super.shouldInterceptRequest(vista, pedido);
                }
                return new WebResourceResponse("text/plain", "utf-8", new ByteArrayInputStream(new byte[0]));
            }
        });

        // El WebView vive dentro de un ScrollView: mientras se toca, desplaza su propio contenido.
        webContenido.setOnTouchListener((v, evento) -> {
            if (evento.getAction() == MotionEvent.ACTION_DOWN) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
            } else if (evento.getAction() == MotionEvent.ACTION_UP
                    || evento.getAction() == MotionEvent.ACTION_CANCEL) {
                v.getParent().requestDisallowInterceptTouchEvent(false);
            }
            return false;
        });
    }

    /** Muestra el contenido segun lo que tenga el recurso: HTML, enlace, ambos o un aviso. */
    private void mostrarVistaPrevia() {
        VistaPreviaRecurso.Contenido contenido = VistaPreviaRecurso.contenidoDe(recurso);
        StringBuilder mensaje = new StringBuilder();

        if (contenido == VistaPreviaRecurso.Contenido.HTML) {
            marcoContenido.setVisibility(View.VISIBLE);
            String documento = VistaPreviaRecurso.documentoHtml(
                    recurso.getContenido(),
                    colorCss(R.color.edu_superficie),
                    colorCss(R.color.edu_texto),
                    colorCss(R.color.edu_primario));
            if (!documento.equals(documentoCargado)) {
                documentoCargado = documento;
                webContenido.loadDataWithBaseURL(null, documento, "text/html", "utf-8", null);
            }
        } else {
            marcoContenido.setVisibility(View.GONE);
            documentoCargado = null;
            webContenido.loadUrl("about:blank");
            if (contenido == VistaPreviaRecurso.Contenido.BLOQUEADO) {
                mensaje.append(getString(R.string.preview_bloqueado));
            }
        }

        boolean hayEnlace = VistaPreviaRecurso.tieneEnlace(recurso);
        boolean enlaceSeguro = VistaPreviaRecurso.esEnlaceHttps(recurso.getUrl());
        if (hayEnlace && !enlaceSeguro) {
            appendLinea(mensaje, getString(R.string.preview_enlace_inseguro));
        }
        if (VistaPreviaRecurso.sinNadaParaMostrar(recurso)) {
            appendLinea(mensaje, getString(R.string.preview_vacio));
        }

        tvPreviewMensaje.setText(mensaje.toString());
        tvPreviewMensaje.setVisibility(mensaje.length() > 0 ? View.VISIBLE : View.GONE);

        btnAbrirEnlace.setVisibility(enlaceSeguro ? View.VISIBLE : View.GONE);
        tvPreviewAviso.setVisibility(enlaceSeguro ? View.VISIBLE : View.GONE);
        if (enlaceSeguro) {
            btnAbrirEnlace.setText(VistaPreviaRecurso.esVideo(recurso)
                    ? R.string.preview_abrir_video : R.string.preview_abrir_enlace);
            final String url = recurso.getUrl().trim();
            btnAbrirEnlace.setOnClickListener(v -> manejarEnlace(url));
        }
    }

    private static void appendLinea(StringBuilder texto, String linea) {
        if (texto.length() > 0) {
            texto.append("\n\n");
        }
        texto.append(linea);
    }

    private String colorCss(int recursoColor) {
        return VistaPreviaRecurso.colorCss(ContextCompat.getColor(this, recursoColor));
    }

    /** Solo se abren enlaces HTTPS, y siempre afuera de la app y con confirmacion. */
    private void manejarEnlace(String url) {
        if (!VistaPreviaRecurso.esEnlaceHttps(url)) {
            Toast.makeText(this, R.string.preview_enlace_bloqueado, Toast.LENGTH_LONG).show();
            return;
        }
        final String destino = url.trim();
        new AlertDialog.Builder(this)
                .setMessage(getString(R.string.preview_abrir_confirmar, VistaPreviaRecurso.hostDe(destino)))
                .setPositiveButton(R.string.preview_abrir, (dialogo, which) -> abrirFuera(destino))
                .setNegativeButton(R.string.form_cancelar, null)
                .show();
    }

    private void abrirFuera(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addCategory(Intent.CATEGORY_BROWSABLE);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, R.string.preview_sin_navegador, Toast.LENGTH_LONG).show();
        }
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
