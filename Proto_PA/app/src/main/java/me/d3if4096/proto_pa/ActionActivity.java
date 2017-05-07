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

public class ActionActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private Switch switchOnOf;
    private ApiClient apiClient;
    private UpdateRelayCallback updateRelayCallback;
    private Toolbar supportActionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        switchOnOf = (Switch) findViewById(R.id.switch_on_of);
        switchOnOf.setEnabled(false);
        apiClient = ApiService.create(new AccountPref(this));
        apiClient.relayStatus().enqueue(new GetRelayStatusCallback());
        updateRelayCallback = new UpdateRelayCallback();

        switchOnOf.setOnClickListener(this);
        switchOnOf.setOnCheckedChangeListener(this);
        switchOnOf.setText(switchOnOf.isChecked() ? "ON" : "OFF");
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.switch_on_of){
            int status = switchOnOf.isChecked() ? 1 : 0;
            apiClient.updateRelay(status).enqueue(updateRelayCallback);
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
        }
    }

    private class GetRelayStatusCallback implements Callback<ApiResponse<RelayStatusResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<RelayStatusResponse>> call, Response<ApiResponse<RelayStatusResponse>> response) {
            if(response.isSuccessful() && response.body().isSuccess()){
                switchOnOf.setChecked(response.body().getData().getStatus() == 1);
                switchOnOf.setEnabled(true);
            }else{
                Toast.makeText(ActionActivity.this, "Tidak dapat mendapatkan status relay", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<RelayStatusResponse>> call, Throwable t) {
            Toast.makeText(ActionActivity.this, "Tidak dapat mendapatkan status relay", Toast.LENGTH_SHORT).show();
        }
    }

    private class UpdateRelayCallback implements Callback<ApiResponse<RelayStatusResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<RelayStatusResponse>> call, Response<ApiResponse<RelayStatusResponse>> response) {
            if(response.isSuccessful() && response.body().isSuccess()){
                switchOnOf.setChecked(response.body().getData().getStatus() == 1);
            }else{
                switchOnOf.setChecked(!switchOnOf.isChecked());
                Toast.makeText(ActionActivity.this, "Tidak dapat mendapatkan status relay", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<RelayStatusResponse>> call, Throwable t) {
            switchOnOf.setChecked(!switchOnOf.isChecked());
            Toast.makeText(ActionActivity.this, "Tidak dapat mendapatkan status relay", Toast.LENGTH_SHORT).show();
        }
    }
}