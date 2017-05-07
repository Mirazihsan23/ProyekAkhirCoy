package me.d3if4096.proto_pa;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class HttpLogin extends Activity {
    /** Called when the activity is first created. */
    private Button buttonlogin;
    private EditText serial, pin;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonlogin = (Button) findViewById(R.id.buttonlogin);
        serial = (EditText) findViewById(R.id.serial);
        pin = (EditText) findViewById(R.id.pin);

        buttonlogin.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String   mSerial = serial.getText().toString();
                String  mPin = pin.getText().toString();

                tryLogin(mSerial, mPin);
            }
        });
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
}