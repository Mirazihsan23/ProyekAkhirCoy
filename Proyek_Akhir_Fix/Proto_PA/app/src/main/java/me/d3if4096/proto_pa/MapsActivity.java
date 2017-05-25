package me.d3if4096.proto_pa;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import me.d3if4096.proto_pa.api.ApiClient;
import me.d3if4096.proto_pa.api.ApiService;
import me.d3if4096.proto_pa.api.response.ApiResponse;
import me.d3if4096.proto_pa.api.response.DeviceLocationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ApiClient apiClient;
    private Thread threadLocation;
    private AccountPref accountPref;
    private DeviceLocationCallback deviceLocationCallback;
    private DangerLocationCallback dangerLocationCallback;
    private LogoutCallback logoutCallback;
    private final String TAG = "serial location request";
    private SettingPref settingPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        settingPref = new SettingPref(this);
        apiClient = ApiService.create(new AccountPref(MapsActivity.this));
        deviceLocationCallback = new DeviceLocationCallback();
        dangerLocationCallback = new DangerLocationCallback();
        logoutCallback = new LogoutCallback();
        accountPref = new AccountPref(this);
        ImageButton btn1 = (ImageButton) findViewById(R.id.profileBtn);
        ImageButton btn2 = (ImageButton) findViewById(R.id.actionBtn);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ProfileActivity.class));
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ActionActivity.class));
            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mMap != null)
            mMap.clear();
        getLocation();
    }

    @Override
    protected void onPause() {
        threadLocation.interrupt();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        threadLocation.interrupt();
        super.onDestroy();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocation();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_logout){
            apiClient.logout().enqueue(logoutCallback);
        }
        else if (item.getItemId()==R.id.action_about){
            startActivity(new Intent(MapsActivity.this,AboutActivity.class));

        }
        return true;
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private void getLocation(){
        if(threadLocation==null){
            threadLocation=new Thread(){
                @Override
                public void run() {
                    while(true){
                        try {
                            String serial = accountPref.getSerial();
                            if (!TextUtils.isEmpty(serial)) {
                                if(settingPref.getRelayMode() == SettingPref.RELAY_DANGER)
                                    apiClient.dangerRoute(serial).enqueue(dangerLocationCallback);
                                else
                                    apiClient.deviceLocation(serial).enqueue(deviceLocationCallback);
                            }
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

            threadLocation.start();
        }
    }

    private void updateLocation(LatLng latLng){
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.locomoto);
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Motorcycle!").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    private void drawRoute(List<LatLng> latLngs){
        mMap.clear();
        mMap.addPolyline(new PolylineOptions().addAll(latLngs).color(Color.RED));
        LatLng latLng = latLngs.get(latLngs.size()-1);
        BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.locomoto);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Your Motorcycle!").icon(icon));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
    }

    private class DeviceLocationCallback implements Callback<ApiResponse<DeviceLocationResponse>>{

        @Override
        public void onResponse(Call<ApiResponse<DeviceLocationResponse>> call, Response<ApiResponse<DeviceLocationResponse>> response) {
            if (response.isSuccessful()&&response.body().isSuccess()){
                LatLng latLng = new LatLng(response.body().getData().getLat(), response.body().getData().getLng());
                updateLocation(latLng);
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<DeviceLocationResponse>> call, Throwable t) {

        }
    }

    private class DangerLocationCallback implements Callback<ApiResponse<List<DeviceLocationResponse>>>{

        @Override
        public void onResponse(Call<ApiResponse<List<DeviceLocationResponse>>> call, Response<ApiResponse<List<DeviceLocationResponse>>> response) {
            if (response.isSuccessful()&&response.body().isSuccess() && response.body().getData().size() > 0){
                List<LatLng> latLngs = new ArrayList<>();
                List<DeviceLocationResponse> routes = response.body().getData();
                for (DeviceLocationResponse d : routes)
                    latLngs.add(new LatLng(d.getLat(), d.getLng()));
                drawRoute(latLngs);
            }
        }

        @Override
        public void onFailure(Call<ApiResponse<List<DeviceLocationResponse>>> call, Throwable t) {

        }
    }

    private class LogoutCallback implements Callback<ApiResponse>{

        @Override
        public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
            if (response.isSuccessful()&& response.body().isSuccess()){

                new AccountPref(MapsActivity.this).logout();
                startActivity(new Intent(MapsActivity.this, MainActivity.class));
                finish();
            }else{
                Toast.makeText(MapsActivity.this,"Logout Failed", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ApiResponse> call, Throwable t) {
            t.printStackTrace();
            Toast.makeText(MapsActivity.this,"Logout Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
