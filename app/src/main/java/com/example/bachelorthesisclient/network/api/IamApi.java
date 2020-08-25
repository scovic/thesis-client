package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.network.dto.UsersDto;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface IamApi {
    @GET("/iam/{id}")
    Single<UsersDto> getUser(@Header("Authorization") String token, @Path("id") int id);
}
