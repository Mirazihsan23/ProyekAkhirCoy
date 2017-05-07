package me.d3if4096.proto_pa;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import me.d3if4096.proto_pa.api.ApiClient;
import me.d3if4096.proto_pa.api.ApiService;
import me.d3if4096.proto_pa.api.response.ApiResponse;
import me.d3if4096.proto_pa.api.response.LoginResponse;
import me.d3if4096.proto_pa.push.notification.GenerateLoginFcmToken;
import me.d3if4096.proto_pa.push.notification.MyFirebaseInstanceIDService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements Callback<ApiResponse<LoginResponse>> {

    private Button buttonlogin;
    private EditText serial, pin;
    private ApiClient apiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        if(new AccountPref(this).hasLoggedIn()){
            startActivity(new Intent(this, MapsActivity.class));
            finish();
        }

        apiClient = ApiService.create();
        serial = (EditText) findViewById(R.id.serial);
        pin = (EditText) findViewById(R.id.code);

        Button btn = (Button) findViewById(R.id.buttonlogin);

        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String mSerial = serial.getText().toString();
                String mPin = pin.getText().toString();
                if(TextUtils.isEmpty(mSerial) || TextUtils.isEmpty(mPin)){
                    Toast.makeText(MainActivity.this, "Lengkapi form", Toast.LENGTH_SHORT).show();
                }else{
                    apiClient.login(mSerial, Integer.parseInt(mPin)).enqueue(MainActivity.this);
                }
            }
        });
    }

    // ieu kuduna menu keur ngalogout

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.action_logout){
            Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_LONG).show();
        }
        return true;
    }

    protected void tryLogin(String mSerial, String mPin)
    {
        HttpURLConnection connection;
        OutputStreamWriter request = null;

        URL url = null;
        String response = null;
        String parameters = "serial="+mSerial+"&pin="+mPin;

        try
        {
            url = new URL("http://8rainy.com/login");
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("POST");

            request = new OutputStreamWriter(connection.getOutputStream());
            request.write(parameters);
            request.flush();
            request.close();
            String line = "";
            InputStreamReader isr = new InputStreamReader(connection.getInputStream());
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line + "\n");
            }
            // Response from server after login process will be stored in response variable.
            response = sb.toString();
            // You can perform UI operations here
            Toast.makeText(this,"Message from Server: \n"+ response, Toast.LENGTH_LONG).show();
            isr.close();
            reader.close();

        }
        catch(IOException e)
        {
            // Error
        }
    }

    @Override
    public void onResponse(Call<ApiResponse<LoginResponse>> call, Response<ApiResponse<LoginResponse>> response) {
        if(response.isSuccessful()){
            if(response.body().isSuccess()){
                new AccountPref(this).store(response.body().getData());
                startService(new Intent(this, GenerateLoginFcmToken.class));
                startActivity(new Intent(this, MapsActivity.class));
                finish();
            }else{
                Toast.makeText(this, response.body().getMessage(), Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Login gagal", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onFailure(Call<ApiResponse<LoginResponse>> call, Throwable t) {
        Toast.makeText(this,"Login gagal", Toast.LENGTH_LONG).show();
    }
}

