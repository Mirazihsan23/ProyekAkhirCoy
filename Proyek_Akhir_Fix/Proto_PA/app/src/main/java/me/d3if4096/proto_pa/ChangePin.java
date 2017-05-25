package me.d3if4096.proto_pa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import me.d3if4096.proto_pa.api.ApiClient;
import me.d3if4096.proto_pa.api.ApiService;
import me.d3if4096.proto_pa.api.response.ApiResponse;
import me.d3if4096.proto_pa.api.response.ProfileResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Miraz Nur Ihsan on 02/05/2017.
 */

public class ChangePin extends AppCompatActivity {

    private ApiClient apiClient;
    private EditText pin, new_pin;
    private ChangePinCallback ChangePinCallback;
    private Button buttonChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        getSupportActionBar().setTitle("Locomoto Mobile");
        apiClient = ApiService.create(new AccountPref(ChangePin.this));
        ChangePinCallback = new ChangePinCallback();

        pin = (EditText) findViewById(R.id.pin);
        new_pin = (EditText) findViewById(R.id.new_pin);

        buttonChange = (Button) findViewById(R.id.buttonupdate);
        buttonChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                apiClient.changePin(
                        pin.getText().toString(),
                        new_pin.getText().toString())
                        .enqueue(ChangePinCallback);

            }
        });
    }

    private class ChangePinCallback implements Callback<ApiResponse> {

        @Override
        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
            if (response.isSuccessful()&& response.body().isSuccess()){
                pin.getText().toString();
                new_pin.getText().toString();

                Toast.makeText(ChangePin.this,"Pin Successfully update", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(ChangePin.this, ProfileActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(ChangePin.this,"Pin Failed update", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse> call, Throwable t) {
            Toast.makeText(ChangePin.this,"Pin can not be updated", Toast.LENGTH_SHORT).show();
        }
    }
}
