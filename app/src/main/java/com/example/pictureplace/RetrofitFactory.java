package com.example.pictureplace;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitFactory {
    public Retrofit newRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ILoginService.BaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
}
