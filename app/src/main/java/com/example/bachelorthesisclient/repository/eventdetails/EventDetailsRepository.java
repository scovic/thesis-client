package com.example.bachelorthesisclient.repository.eventdetails;

import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.model.EventObject;
import com.example.bachelorthesisclient.model.Performer;

import java.util.List;

import io.reactivex.Single;

public interface EventDetailsRepository {
    Single<EventDetails> getEventDetails();

    Single<List<EventObject>> getEventObjects();

    Single<List<Performer>> getStagePerformers(int stageId);
}
