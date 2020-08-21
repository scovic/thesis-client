package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.model.BasicLoginResponse;
import com.example.bachelorthesisclient.model.RegisterResponse;

import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthApi {
    @FormUrlEncoded
    @POST("/iam/login")
    Single<BasicLoginResponse> login(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("/iam")
    Single<RegisterResponse> register(
        @Field("email") String email,
        @Field("firstName") String firstName,
        @Field("lastName") String lastName,
        @Field("password") String password
    );
}
