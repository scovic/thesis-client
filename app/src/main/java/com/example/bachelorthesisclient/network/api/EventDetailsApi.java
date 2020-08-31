package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.network.dto.EventObjectsDto;
import com.example.bachelorthesisclient.network.dto.PerformersDto;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface EventDetailsApi {
    @GET("/event-details")
    Single<EventObjectsDto> getEventObjects(
            @Header("Authorization") String token
    );

    @GET("/event-details/{stageId}")
    Single<PerformersDto> getStagePerformers(
            @Header("Authorization") String token,
            @Path("stageId") int stageId
    );
}
