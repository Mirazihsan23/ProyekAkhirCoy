package me.d3if4096.proto_pa;

import android.app.Activity;
import android.content.Intent;
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
 * Created by Koesuma on 21/02/2017.
 */

public class ProfileActivity extends Activity {

    private Button buttonchange;
    private Button buttonchangepin;
    private ApiClient apiClient;
    private EditText nama, plat, no_hp;
    private ProfileCallBack profileCallBack;
    private UpdateProfileCallBack updateProfileCallBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        apiClient = ApiService.create(new AccountPref(ProfileActivity.this));
        profileCallBack = new ProfileCallBack();
        updateProfileCallBack = new UpdateProfileCallBack();

        nama = (EditText) findViewById(R.id.nama);
        plat = (EditText) findViewById(R.id.plat);
        no_hp = (EditText) findViewById(R.id.no_hp);

        buttonchange = (Button) findViewById(R.id.updateProfile);
        buttonchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiClient.changeProfile(
                        nama.getText().toString(),
                        plat.getText().toString(),
                        no_hp.getText().toString())
                        .enqueue(updateProfileCallBack);
            }
        });
        apiClient.getProfile().enqueue(profileCallBack);

        buttonchangepin = (Button) findViewById(R.id.changepin);
        buttonchangepin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ChangePin.class);
                startActivity(intent);
            }
        });
    }

    private class ProfileCallBack implements Callback<ApiResponse<ProfileResponse>> {

        @Override
        public void onResponse(Call<ApiResponse<ProfileResponse>> call, Response<ApiResponse<ProfileResponse>> response) {
            if (response.isSuccessful()&& response.body().isSuccess()){
                nama.setText(response.body().getData().getNama());
                plat.setText(response.body().getData().getPlat());
                no_hp.setText(response.body().getData().getNo_hp());
            }else{
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<ProfileResponse>> call, Throwable t) {
        }
    }

    private class UpdateProfileCallBack implements Callback<ApiResponse<ProfileResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<ProfileResponse>> call, Response<ApiResponse<ProfileResponse>> response) {
            if (response.isSuccessful()&& response.body().isSuccess()){
                nama.getText().toString();
                plat.getText().toString();
                no_hp.getText().toString();

                Toast.makeText(ProfileActivity.this,"Profile Berhasil Update", Toast.LENGTH_LONG).show();
            }else{

            }
        }

        @Override
        public void onFailure(Call<ApiResponse<ProfileResponse>> call, Throwable t) {
            Toast.makeText(ProfileActivity.this,"Profile Gagal Update", Toast.LENGTH_LONG).show();
        }
    }
}
