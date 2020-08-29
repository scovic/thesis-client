package com.example.bachelorthesisclient.network.api;

import com.example.bachelorthesisclient.network.dto.TicketsDto;

import io.reactivex.Single;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface TicketApi {
    @FormUrlEncoded
    @POST("/tickets")
    Single<TicketsDto> purchaseTickets(
            @Header("Authorization") String token,
            @Field("userId") int userId,
            @Field("quantity") int quantity
    );

    @GET("/tickets/{id}")
    Single<TicketsDto> getUserTickets(
            @Header("Authorization") String token,
            @Path("id") int userId
    );

    @DELETE("/tickets/{id}")
    Single<Boolean> cancelReservation(
            @Header("Authorization") String token,
            @Path("id") int ticketId
    );
}
