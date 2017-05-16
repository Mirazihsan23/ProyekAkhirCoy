package me.d3if4096.proto_pa;

import android.app.Activity;
import android.os.Bundle;
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

public class ChangePin extends Activity {

    private ApiClient apiClient;
    private EditText pin, new_pin;
    private ChangePinCallback ChangePinCallback;
    private Button buttonChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);

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

                Toast.makeText(ChangePin.this,"Pin Berhasil Update", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(ChangePin.this,"Pin Gagal Update", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse> call, Throwable t) {
            Toast.makeText(ChangePin.this,"Pin Tidak Bisa Diupdate", Toast.LENGTH_LONG).show();
        }
    }
}
