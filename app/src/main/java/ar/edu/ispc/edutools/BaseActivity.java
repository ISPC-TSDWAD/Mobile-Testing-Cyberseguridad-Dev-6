package ar.edu.ispc.edutools;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
}
