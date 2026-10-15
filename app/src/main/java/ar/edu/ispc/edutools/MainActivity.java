package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

import ar.edu.ispc.edutools.model.UsuarioItem;
import ar.edu.ispc.edutools.security.SessionManager;
import ar.edu.ispc.edutools.util.JwtUtil;
import ar.edu.ispc.edutools.util.TemaApp;

/**
 * Home de la aplicacion. Padre de todos los modulos, concentra la navegacion
 * principal mediante tarjetas. Recibe por Intent el usuario cargado en el login.
 */
public class MainActivity extends BaseActivity {

    /** Clave para reenviar el usuario a las pantallas hijas. */
    public static final String EXTRA_USUARIO = LoginActivity.EXTRA_USUARIO;

    private String usuario = "invitado";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurarToolbar(false);   // Activity raiz del area autenticada.

        // Dato enviado desde LoginActivity.
        Intent origen = getIntent();
        if (origen != null && origen.hasExtra(EXTRA_USUARIO)) {
            usuario = origen.getStringExtra(EXTRA_USUARIO);
        }

        TextView tvBienvenida = findViewById(R.id.tvBienvenida);
        TextView tvRol = findViewById(R.id.tvRol);
        tvBienvenida.setText(getString(R.string.home_bienvenida, usuario));
        // El rol sale del token que guardo el login (Issue #19).
        final SessionManager sesion = new SessionManager(this);
        String rol = JwtUtil.getRol(sesion.getAccessToken());
        tvRol.setText(getString(R.string.home_rol, UsuarioItem.nombreDeRol(rol)));

        // Navegacion hacia los modulos hijos.
        CardView cardRecursos = findViewById(R.id.cardRecursos);
        CardView cardUsuarios = findViewById(R.id.cardUsuarios);
        CardView cardMultimedia = findViewById(R.id.cardMultimedia);
        CardView cardContacto = findViewById(R.id.cardContacto);
        CardView cardAcerca = findViewById(R.id.cardAcerca);
        Button btnCerrarSesion = findViewById(R.id.btnCerrarSesion);
        CardView cardTema = findViewById(R.id.cardTema);

        cardRecursos.setOnClickListener(v -> abrir(RecursosActivity.class));
        cardUsuarios.setOnClickListener(v -> abrir(UsuariosActivity.class));
        cardMultimedia.setOnClickListener(v -> abrir(MultimediaActivity.class));
        cardContacto.setOnClickListener(v -> abrir(ContactoActivity.class));
        cardAcerca.setOnClickListener(v -> abrir(AcercaDeActivity.class));

        // Selector de tema: Claro / Oscuro / Segun el sistema.
        mostrarTemaActual();
        cardTema.setOnClickListener(v -> mostrarSelectorTema());

        btnCerrarSesion.setOnClickListener(v -> {
            sesion.cerrarSesion();   // borra los tokens guardados
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    private void mostrarTemaActual() {
        TextView tvTemaActual = findViewById(R.id.tvTemaActual);
        String[] nombres = getResources().getStringArray(R.array.tema_opciones);
        int indice = TemaApp.indice(TemaApp.leer(this));
        tvTemaActual.setText(getString(R.string.home_tema_actual, nombres[indice]));
    }

    /** La eleccion se guarda y se aplica al instante en todas las pantallas. */
    private void mostrarSelectorTema() {
        int actual = TemaApp.indice(TemaApp.leer(this));
        new AlertDialog.Builder(this)
                .setTitle(R.string.tema_titulo)
                .setSingleChoiceItems(R.array.tema_opciones, actual, (dialog, which) -> {
                    TemaApp.elegir(this, TemaApp.valorEn(which));
                    dialog.dismiss();
                    mostrarTemaActual();
                })
                .setNegativeButton(R.string.form_cancelar, null)
                .show();
    }

    /** Abre una pantalla hija propagando el usuario de la sesion. */
    private void abrir(Class<?> destino) {
        Intent intent = new Intent(MainActivity.this, destino);
        intent.putExtra(EXTRA_USUARIO, usuario);
        startActivity(intent);
    }
}
