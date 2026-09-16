package ar.edu.ispc.edutools;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;

/**
 * Guia multimedia con los estandares institucionales de EduTools.
 * Activity hija de MainActivity.
 *
 * El video se resuelve por nombre dentro de res/raw. Si el archivo no esta
 * presente se oculta el reproductor y se muestra un aviso, de modo que la
 * pantalla nunca falla.
 */
public class MultimediaActivity extends BaseActivity {

    private static final String NOMBRE_VIDEO = "presentacion_edutools";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multimedia);
        configurarToolbar(true);

        VideoView videoView = findViewById(R.id.videoPresentacion);
        TextView tvAvisoVideo = findViewById(R.id.tvAvisoVideo);

        int idVideo = getResources().getIdentifier(NOMBRE_VIDEO, "raw", getPackageName());

        if (idVideo != 0) {
            Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + idVideo);
            videoView.setVideoURI(uri);

            MediaController controles = new MediaController(this);
            controles.setAnchorView(videoView);
            videoView.setMediaController(controles);

            // Accesibilidad: el reproductor se anuncia como elemento propio.
            videoView.setContentDescription(getString(R.string.cd_video));

            videoView.setVisibility(View.VISIBLE);
            tvAvisoVideo.setVisibility(View.GONE);
        } else {
            videoView.setVisibility(View.GONE);
            tvAvisoVideo.setVisibility(View.VISIBLE);
        }
    }
}
