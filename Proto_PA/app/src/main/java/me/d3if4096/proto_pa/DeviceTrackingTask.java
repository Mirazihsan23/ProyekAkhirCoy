package me.d3if4096.proto_pa;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import me.d3if4096.proto_pa.api.ApiClient;
import me.d3if4096.proto_pa.api.ApiService;
import me.d3if4096.proto_pa.api.response.ApiResponse;
import me.d3if4096.proto_pa.api.response.DeviceLocationResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 */
public class DeviceTrackingTask implements Callback<ApiResponse<DeviceLocationResponse>> {
    private LatLng latLng;
    private int radius;
    private OnDataUpdateListener onDataUpdateListener;
    private ApiClient apiClient;
    private AccountPref accountPref;

    public DeviceTrackingTask(Context context, LatLng latLng, int radius) {
        this.latLng = latLng;
        this.radius = radius;
        accountPref = new AccountPref(context);
        apiClient = ApiService.create(new AccountPref(context));
    }

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        this.onDataUpdateListener = onDataUpdateListener;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void exec(){
        request();
    }

    private void request(){
        apiClient.deviceLocation(accountPref.getSerial()).enqueue(this);
    }

    @Override
    public void onResponse(Call<ApiResponse<DeviceLocationResponse>> call, Response<ApiResponse<DeviceLocationResponse>> response) {
        if(response.isSuccessful() && onDataUpdateListener != null){
            DeviceLocationResponse device = response.body().getData();
            LatLng latLng = new LatLng(device.getLat(),device.getLng());
            onDataUpdateListener.onDataUpdated(latLng);
        }
        new WaitingTask().execute();
    }

    @Override
    public void onFailure(Call<ApiResponse<DeviceLocationResponse>> call, Throwable t) {
        new WaitingTask().execute();
    }

    private class WaitingTask extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {
            Thread.currentThread();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            request();
        }
    }

    public interface OnDataUpdateListener{
        public void onDataUpdated(LatLng latLng);
    }
}
