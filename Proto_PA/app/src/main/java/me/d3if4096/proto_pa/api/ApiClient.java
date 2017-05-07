package me.d3if4096.proto_pa.api;

import me.d3if4096.proto_pa.api.response.ApiResponse;
import me.d3if4096.proto_pa.api.response.DeviceLocationResponse;
import me.d3if4096.proto_pa.api.response.LoginResponse;
import me.d3if4096.proto_pa.api.response.ProfileResponse;
import me.d3if4096.proto_pa.api.response.RelayStatusResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Miraz Nur Ihsan on 29/04/2017.
 */

public interface ApiClient {

    @FormUrlEncoded
    @POST("/firebase/update")
    Call<ApiResponse<Object>> updateFcmKey(@Field("fcm_key") String fcmKey);

    @FormUrlEncoded
    @POST("/login")
    Call<ApiResponse<LoginResponse>> login(@Field("serial") String serial, @Field("pin") int pin);

    @GET ("/device/{serial}/lat/lng")
    Call<ApiResponse<DeviceLocationResponse>> deviceLocation(@Path("serial") String serial);

    @FormUrlEncoded
    @POST("/device/relay/update")
    Call<ApiResponse<RelayStatusResponse>> updateRelay(@Field("status") int status);

    @GET("/device/relay/status")
    Call<ApiResponse<RelayStatusResponse>> relayStatus();

    @POST("/logout")
    Call<ApiResponse> logout();

    @GET("/user/profile")
    Call<ApiResponse<ProfileResponse>> getProfile();

    @FormUrlEncoded
    @POST("/user/profile/update")
    Call<ApiResponse<ProfileResponse>> changeProfile(@Field("nama") String nama, @Field("plat") String plat, @Field("no_hp") String no_hp);

}
