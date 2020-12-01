package com.koopers.techservice.data.remote;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitApi {
    private static RetrofitApi instance;

    private Retrofit retrofit;

    public static RetrofitApi getInstance() {
        if (instance == null) {
            instance = new RetrofitApi();
        }

        return instance;
    }

    public RetrofitApi() {
        buildRetrofit();
    }

    private void buildRetrofit() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS).build();

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConstants.getURLBASE())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public void NukeInstance(){
        retrofit = null;
    }
}