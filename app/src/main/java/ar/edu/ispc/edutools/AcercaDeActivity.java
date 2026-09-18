package ar.edu.ispc.edutools;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;

/** Pantalla institucional "Quienes somos". Activity hija de MainActivity. */
public class AcercaDeActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acerca_de);
        configurarToolbar(true);

        Button btnCopiar = findViewById(R.id.btnCopiarSoporte);
        if (btnCopiar != null) {
            btnCopiar.setOnClickListener(v -> copiarEmailSoporte());
        }
    }

    private void copiarEmailSoporte() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboard != null) {
            ClipData clip = ClipData.newPlainText("Mesa de ayuda EduTools", "soporte@edutools.edu.ar");
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, getString(R.string.acerca_email_copiado), Toast.LENGTH_SHORT).show();
        }
    }
}
