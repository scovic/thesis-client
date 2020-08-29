package com.example.bachelorthesisclient.repository.eventdetails;

import com.example.bachelorthesisclient.model.EventDetails;

import io.reactivex.Single;

public interface EventDetailsRepository {
    Single<EventDetails> getEventDetails();
}
