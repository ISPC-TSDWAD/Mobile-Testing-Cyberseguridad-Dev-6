package ar.edu.ispc.edutools;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ar.edu.ispc.edutools.net.ApiCallback;
import ar.edu.ispc.edutools.net.ApiErrorMapper;
import ar.edu.ispc.edutools.security.SessionManager;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Activity base de EduTools Mobile. Centraliza la Toolbar y la navegacion Up.
 * El padre de cada pantalla se declara con android:parentActivityName en el manifest.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private boolean redirigiendoAlLogin;

    /** Borra la sesion vencida y evita volver a pantallas autenticadas con Atras. */
    protected void cerrarSesionPorVencimiento() {
        if (redirigiendoAlLogin || isFinishing() || isDestroyed()) {
            return;
        }
        redirigiendoAlLogin = true;
        new SessionManager(this).cerrarSesion();
        Toast.makeText(this, ApiErrorMapper.mensaje(401, null), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    /** Para llamadas protegidas: un 401 cierra la sesion; un 403 conserva la sesion. */
    protected abstract class CallbackConSesion<T> extends ApiCallback<T> {
        @Override
        public boolean onSesionVencida() {
            cerrarSesionPorVencimiento();
            return true;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> respuesta) {
            if (!isFinishing() && !isDestroyed()) {
                super.onResponse(call, respuesta);
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable error) {
            if (!isFinishing() && !isDestroyed() && !call.isCanceled()) {
                super.onFailure(call, error);
            }
        }
    }

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
}
