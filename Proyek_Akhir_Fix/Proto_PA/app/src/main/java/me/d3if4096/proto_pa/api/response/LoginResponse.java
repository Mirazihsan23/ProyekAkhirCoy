package me.d3if4096.proto_pa.api.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Miraz Nur Ihsan on 29/04/2017.
 */

public class LoginResponse {
    @SerializedName("serial")
    @Expose
    private String serial;
    @SerializedName("pin")
    @Expose
    private String pin;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;
    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("relay")
    @Expose
    private Integer relay;

    public String getSerial() {
        return serial;
    }

    public String getPin() {
        return pin;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getToken() {
        return token;
    }

    public Integer getRelay() {
        return relay;
    }
}
