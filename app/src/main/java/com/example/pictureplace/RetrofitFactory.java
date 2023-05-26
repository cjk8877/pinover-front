package com.example.pictureplace;

import android.content.Context;
import android.util.Log;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    public static Retrofit createRetrofit(Context context,String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(createOkHttpClient())
                .build();
        return retrofit;
    }

    private static OkHttpClient createOkHttpClient() {
        SelfSigningHelper helper = SelfSigningHelper.getInstance();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        helper.setSSLOkHttp(builder,"www.picplace.kro.kr");

        return builder.build();
    }
}
