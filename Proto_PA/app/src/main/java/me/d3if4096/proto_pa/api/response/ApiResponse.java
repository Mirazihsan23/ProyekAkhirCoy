package me.d3if4096.proto_pa.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Miraz Nur Ihsan on 29/04/2017.
 */

public class ApiResponse<T> {
    @SerializedName("status")
    private boolean success;
    private String message;
    private T data;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
