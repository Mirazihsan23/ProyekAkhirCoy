package me.d3if4096.proto_pa;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import me.d3if4096.proto_pa.api.ApiClient;
import me.d3if4096.proto_pa.api.ApiService;
import me.d3if4096.proto_pa.api.response.ApiResponse;
import me.d3if4096.proto_pa.api.response.RelayStatusResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Koesuma on 11/04/2017.
 */

public class ActionActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch switchOnOf;
    private ApiClient apiClient;
    private TextView status1, status2;
    private UpdateRelayCallback updateRelayCallback;
    private TurnOffDangeryCallback turnOffDangeryCallback;
    private Toolbar supportActionBar;
    private SettingPref settingPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);
        getSupportActionBar().setTitle("Locomoto Mobile");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        status1= (TextView) findViewById(R.id.status1);
        status2= (TextView) findViewById(R.id.status2);

        switchOnOf = (Switch) findViewById(R.id.switch_on_of);
        switchOnOf.setEnabled(false);
        apiClient = ApiService.create(new AccountPref(this));
        apiClient.relayStatus().enqueue(new GetRelayStatusCallback());
        updateRelayCallback = new UpdateRelayCallback();
        turnOffDangeryCallback = new TurnOffDangeryCallback();
        status1.setText(switchOnOf.isChecked() ? getString(R.string.status_on) : getString(R.string.status_off));
        status2.setText(switchOnOf.isChecked() ? getString(R.string.status_on2) : getString(R.string.status_off2));

        switchOnOf.setOnClickListener(this);
        switchOnOf.setOnCheckedChangeListener(this);
        switchOnOf.setText(switchOnOf.isChecked() ? "ON" : "OFF");
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingPref = new SettingPref(this);
        if(settingPref.getRelayMode() == SettingPref.RELAY_DANGER){
            findViewById(R.id.layoutbahaya).setVisibility(View.VISIBLE);
            findViewById(R.id.buttonbahaya).setOnClickListener(this);
        }else{
            findViewById(R.id.layoutbahaya).setVisibility(View.INVISIBLE);
            findViewById(R.id.buttonbahaya).setOnClickListener(null);
        }
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.switch_on_of){
            int status = switchOnOf.isChecked() ? 1 : 0;
            apiClient.updateRelay(status).enqueue(updateRelayCallback);
        }else if(view.getId() == R.id.buttonbahaya){
            apiClient.turnOffDanger().enqueue(turnOffDangeryCallback);
        }
    }

    public void setSupportActionBar(Toolbar supportActionBar) {
        this.supportActionBar = supportActionBar;
        setTitle(R.string.control);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if(compoundButton.getId() == R.id.switch_on_of){
            switchOnOf.setText(switchOnOf.isChecked() ? "ON" : "OFF");
            status1.setText(switchOnOf.isChecked() ? getString(R.string.status_on) : getString(R.string.status_off));
            status2.setText(switchOnOf.isChecked() ? getString(R.string.status_on2) : getString(R.string.status_off2));

            if(settingPref.getRelayMode() != SettingPref.RELAY_DANGER){
                if(switchOnOf.isChecked())
                    settingPref.turnOnRelay();
                else
                    settingPref.turnOffRelay();
            }
        }
    }

    private class GetRelayStatusCallback implements Callback<ApiResponse<RelayStatusResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<RelayStatusResponse>> call, Response<ApiResponse<RelayStatusResponse>> response) {
            if(response.isSuccessful() && response.body().isSuccess()){
                switchOnOf.setChecked(response.body().getData().getStatus() == 1);
                switchOnOf.setEnabled(true);
            }else{
                Toast.makeText(ActionActivity.this, "Can not get relay status", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<RelayStatusResponse>> call, Throwable t) {
            Toast.makeText(ActionActivity.this, "Can not get relay status", Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateRelayCallback implements Callback<ApiResponse<RelayStatusResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<RelayStatusResponse>> call, Response<ApiResponse<RelayStatusResponse>> response) {
            if(response.isSuccessful() && response.body().isSuccess()){
                switchOnOf.setChecked(response.body().getData().getStatus() == 1);
            }else{
                switchOnOf.setChecked(!switchOnOf.isChecked());
                Toast.makeText(ActionActivity.this, "Can not get relay status", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<RelayStatusResponse>> call, Throwable t) {
            switchOnOf.setChecked(!switchOnOf.isChecked());
            Toast.makeText(ActionActivity.this, "Can not get relay status", Toast.LENGTH_SHORT).show();
        }
    }

    private class TurnOffDangeryCallback implements  Callback<ApiResponse<RelayStatusResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<RelayStatusResponse>> call, Response<ApiResponse<RelayStatusResponse>> response) {
            if(response.isSuccessful() && response.body().isSuccess()){
                findViewById(R.id.layoutbahaya).setVisibility(View.GONE);
                switchOnOf.setChecked(response.body().getData().getStatus() == 1);
                switchOnOf.setEnabled(true);
                if(switchOnOf.isChecked())
                    settingPref.turnOnRelay();
                else
                    settingPref.turnOffRelay();
            }else{
                Toast.makeText(ActionActivity.this, "Can not turn off danger", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<RelayStatusResponse>> call, Throwable t) {
            Toast.makeText(ActionActivity.this, "Can not turn off danger", Toast.LENGTH_SHORT).show();
        }
    }
}