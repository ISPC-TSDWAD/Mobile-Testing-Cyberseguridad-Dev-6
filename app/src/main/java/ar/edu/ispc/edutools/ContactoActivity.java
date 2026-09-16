package ar.edu.ispc.edutools;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;

/**
 * Pantalla de contacto con la mesa de ayuda de EduTools.
 * Activity hija de MainActivity.
 */
public class ContactoActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacto);
        configurarToolbar(true);

        Button btnEnviar = findViewById(R.id.btnEnviarConsulta);
        btnEnviar.setOnClickListener(v -> finish());
    }
}
