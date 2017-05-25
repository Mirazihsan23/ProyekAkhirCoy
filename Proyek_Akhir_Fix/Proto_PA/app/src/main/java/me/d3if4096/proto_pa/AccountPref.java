package me.d3if4096.proto_pa;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import me.d3if4096.proto_pa.api.response.LoginResponse;

/**
 * Created by Miraz Nur Ihsan on 29/04/2017.
 */

public class AccountPref {
    private final String PREF_NAME = "account";
    private final String KEY_SERIAL = "serial";
    private final String KEY_PIN = "pin";
    private final String KEY_LAT = "lat";
    private final String KEY_LNG = "lng";
    private final String KEY_TOKEN = "token";
    private final String KEY_RELAY = "relay";
    private SharedPreferences sharedPreferences;

    public AccountPref(Context context){
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void store(LoginResponse loginResponse){
        sharedPreferences.edit()
                .putString(KEY_SERIAL, loginResponse.getSerial())
                .putString(KEY_PIN, loginResponse.getPin())
                .putString(KEY_TOKEN, loginResponse.getToken())
                .putInt(KEY_RELAY, loginResponse.getRelay())
                .apply();
    }

    public boolean hasLoggedIn(){
        return !TextUtils.isEmpty(sharedPreferences.getString(KEY_TOKEN, ""));
    }

    public String getSerial(){
        return sharedPreferences.getString(KEY_SERIAL,"");
    }

    public String getToken(){
        return sharedPreferences.getString(KEY_TOKEN,"");
    }

    public void logout(){
        sharedPreferences.edit().clear().apply();
    }
}
