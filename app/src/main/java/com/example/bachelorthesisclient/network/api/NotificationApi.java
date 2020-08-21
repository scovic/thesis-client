package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.network.dto.NotificationDto;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface NotificationApi {
    @POST("/notifications/info")
    Single<Boolean> sendInfoNotification(
            @Header("Authorization") String token,
            @Body NotificationDto notificationDto
    );
}
