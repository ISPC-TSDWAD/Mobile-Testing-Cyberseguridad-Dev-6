package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ar.edu.ispc.edutools.net.ApiErrorMapper;
import ar.edu.ispc.edutools.security.SessionManager;

/**
 * Activity base de EduTools Mobile. Centraliza la Toolbar y la navegacion Up.
 * El padre de cada pantalla se declara con android:parentActivityName en el manifest.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Engancha la Toolbar del layout y habilita la flecha de retroceso.
     *
     * @param mostrarUp true en las Activity hijas; false en las raices.
     */
    protected void configurarToolbar(boolean mostrarUp) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(mostrarUp);
            actionBar.setDisplayShowHomeEnabled(mostrarUp);
        }
    }

    /** Gestiona el toque sobre la flecha de la Toolbar. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Respuesta unica ante un 401 en una pantalla con sesion: borra los tokens,
     * avisa y vuelve al Login sin dejar pantallas atras.
     */
    protected void cerrarSesionPorVencimiento() {
        new SessionManager(this).cerrarSesion();
        Toast.makeText(this, ApiErrorMapper.mensaje(401, null), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
