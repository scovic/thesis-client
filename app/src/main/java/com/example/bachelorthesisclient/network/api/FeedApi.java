package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.network.dto.FeedsDto;
import com.example.bachelorthesisclient.network.dto.PostDto;

import io.reactivex.Single;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface FeedApi {

    @GET("/post-service")
    Single<FeedsDto> getFeeds(@Header("Authorization") String token);

    @GET("/post-service/{postId}")
    Single<PostDto> getPost(@Header("Authorization") String token, @Path("postId") int postId);

    @GET("/post-service/{postId}/{fileName}")
    Single<ResponseBody> getFile(
            @Header("Authorization") String token,
            @Path("postId") int postId,
            @Path("fileName") String fileName
    );


    @POST("/post-service")
    Single<PostDto> createFeed(
            @Header("Authorization") String token,
            @Body RequestBody requestBody
    );
}
