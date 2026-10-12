package ar.edu.ispc.edutools.net;

import com.google.gson.annotations.SerializedName;

/** Respuesta de api/token/: los dos tokens, el rol y si es staff. */
public class LoginResponse {

    private String access;
    private String refresh;
    private String rol;

    @SerializedName("is_staff")
    private boolean isStaff;

    public String getAccess() { return access; }

    public String getRefresh() { return refresh; }

    public String getRol() { return rol; }

    public boolean isStaff() { return isStaff; }
}
