package me.d3if4096.proto_pa.push.notification;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import me.d3if4096.proto_pa.AccountPref;
import me.d3if4096.proto_pa.api.ApiClient;
import me.d3if4096.proto_pa.api.ApiService;
import me.d3if4096.proto_pa.api.response.ApiResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miraz Nur Ihsan on 03/05/2017.
 */

public class GenerateLoginFcmToken extends IntentService {
    private static final String TAG = "GenerateLoginFcmToken";
    public GenerateLoginFcmToken() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        AccountPref accountPref = new AccountPref(this);
        ApiClient apiClient = ApiService.create(accountPref);
        if(accountPref.hasLoggedIn()){
            apiClient.updateFcmKey(token).enqueue(new Callback<ApiResponse<Object>>() {
                @Override
                public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                    Log.d(TAG, new Gson().toJson(response));
                }

                @Override
                public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                    Log.d(TAG, t.getMessage());
                }
            });
        }
    }
}
