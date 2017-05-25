package me.d3if4096.proto_pa.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Miraz Nur Ihsan on 05/05/2017.
 */

public class ProfileResponse {
    @SerializedName("nama")
    @Expose
    private String nama;
    @SerializedName("plat")
    @Expose
    private String plat;
    @SerializedName("no_hp")
    @Expose
    private String no_hp;

    public String getNama() {
        return nama;
    }

    public String getPlat() {
        return plat;
    }

    public String getNo_hp() {
        return no_hp;
    }
}
