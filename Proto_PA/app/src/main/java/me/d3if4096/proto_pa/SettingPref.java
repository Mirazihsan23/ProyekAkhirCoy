package me.d3if4096.proto_pa;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Miraz Nur Ihsan on 15/05/2017.
 */

public class SettingPref {
    public static final int RELAY_OFF = 0;
    public static final int RELAY_ON = 1;
    public static final int RELAY_DANGER = 2;

    private final String PREF_NAME = "setting";
    private final String SETTING_RELAY = "relay";

    private SharedPreferences sharedPreferences;

    public SettingPref(Context context){
        this.sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void turnOffRelay(){
        setSetting(SETTING_RELAY, RELAY_OFF);
    }

    public void turnOnRelay(){
        setSetting(SETTING_RELAY, RELAY_ON);
    }

    public void turnRelayDanger(){
        setSetting(SETTING_RELAY, RELAY_DANGER);
    }

    public int getRelayMode(){
        return sharedPreferences.getInt(SETTING_RELAY, RELAY_OFF);
    }

    private void setSetting(String settingName, int val){
        sharedPreferences.edit()
                .putInt(settingName, val)
                .apply();;
    }
}
