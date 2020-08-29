package com.example.bachelorthesisclient.repository.eventdetails;

import com.example.bachelorthesisclient.datasource.local.EventDetailsLocalDataSource;
import com.example.bachelorthesisclient.model.EventDetails;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class EventDetailsRepositoryImpl implements EventDetailsRepository {
    private EventDetailsLocalDataSource localDataSource;
    private static EventDetailsRepository instance;

    public static EventDetailsRepository getInstance() {
        if (instance == null) {
            instance = new EventDetailsRepositoryImpl();
        }

        return instance;
    }

    private EventDetailsRepositoryImpl() {
        localDataSource = EventDetailsLocalDataSource.getInstance();
    }

    @Override
    public Single<EventDetails> getEventDetails() {
        return localDataSource.getEventDetails()
                .subscribeOn(Schedulers.io());
    }
}
