package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.network.dto.UserDto;
import com.example.bachelorthesisclient.network.dto.UsersDto;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IamApi {
    @GET("/iam/{id}")
    Single<UsersDto> getUser(@Header("Authorization") String token, @Path("id") int id);

    @FormUrlEncoded
    @PUT("/iam/{id}")
    Single<Boolean> updateUser(
            @Header("Authorization") String token,
            @Path("id") int id,
            @Field("firstName") String firstName,
            @Field("lastName") String lastName
        );
}
