package me.d3if4096.proto_pa.api;

import android.text.TextUtils;

import java.io.IOException;

import me.d3if4096.proto_pa.AccountPref;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Miraz Nur Ihsan on 29/04/2017.
 */

public class ApiService {
    public static ApiClient create(){
        return create(null);
    }

    public static ApiClient create(final AccountPref accountPref){
        String API_BASE_URL = "http://8rainy.com/";
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if(accountPref != null && !TextUtils.isEmpty(accountPref.getToken())){
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("token", accountPref.getToken())
                            .method(original.method(), original.body())
                            .build();

                    return chain.proceed(request);
                }
            });
        }

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(API_BASE_URL)
                        .addConverterFactory(
                                GsonConverterFactory.create()
                        );

        Retrofit retrofit =
                builder
                        .client(
                                httpClient.build()
                        )
                        .build();

        return retrofit.create(ApiClient.class);
    }
}
