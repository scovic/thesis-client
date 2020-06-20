package com.example.bachelorthesisclient.wrapper;

import com.example.bachelorthesisclient.BuildConfig;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitWrapper {
    private Retrofit retrofit;
    private static RetrofitWrapper instance;

    private RetrofitWrapper() {
        retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.BACKEND_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Retrofit getRetrofit() {
        return retrofit;
    }

    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new RetrofitWrapper();
        }

        return instance.getRetrofit();
    }

}
