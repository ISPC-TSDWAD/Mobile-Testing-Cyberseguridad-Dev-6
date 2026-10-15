package ar.edu.ispc.edutools.security;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * Guarda los tokens cifrados con EncryptedSharedPreferences (Issue #26).
 * La clave maestra vive en el Android Keystore y las claves y los valores
 * se cifran con AES-256. Pide Android 6 (API 23) o mas.
 * Nunca se escriben los tokens en ningun log.
 */
public class EncryptedTokenStorage implements TokenStorage {

    private static final String ARCHIVO = "edutools_sesion_segura";
    private static final String CLAVE_ACCESS = "access";
    private static final String CLAVE_REFRESH = "refresh";
    private static final String CLAVE_USUARIO = "usuario";

    private final SharedPreferences prefs;

    public EncryptedTokenStorage(Context context) throws GeneralSecurityException, IOException {
        String claveMaestra = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        prefs = EncryptedSharedPreferences.create(
                ARCHIVO,
                claveMaestra,
                context.getApplicationContext(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM);
    }

    @Override
    public void saveTokens(String access, String refresh) {
        prefs.edit().putString(CLAVE_ACCESS, access).putString(CLAVE_REFRESH, refresh).apply();
    }

    @Override
    public String getAccessToken() {
        return prefs.getString(CLAVE_ACCESS, null);
    }

    @Override
    public String getRefreshToken() {
        return prefs.getString(CLAVE_REFRESH, null);
    }

    @Override
    public void saveUsername(String username) {
        prefs.edit().putString(CLAVE_USUARIO, username).apply();
    }

    @Override
    public String getUsername() {
        return prefs.getString(CLAVE_USUARIO, null);
    }

    @Override
    public void clear() {
        prefs.edit().clear().apply();
    }
}
