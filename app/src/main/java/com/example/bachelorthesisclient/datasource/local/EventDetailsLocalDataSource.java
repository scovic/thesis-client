package com.example.bachelorthesisclient.datasource.local;

import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.util.EventDetailsPersistenceUtil;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

import io.reactivex.Single;

public class EventDetailsLocalDataSource {
    private final String name = "Super Nish Event";
    private final String eventDateTimestamp = "1603393200000";
    private final GeoPoint location = new GeoPoint(43.3260, 21.8954);

    private static EventDetailsLocalDataSource instance;

    public static EventDetailsLocalDataSource getInstance() {
        if (instance == null) {
            instance = new EventDetailsLocalDataSource();
        }

        return instance;
    }

    private EventDetailsLocalDataSource() {
    }

    public Single<EventDetails> getEventDetails() {
        return Single.just(new EventDetails(location, name, new Date(Long.parseLong(eventDateTimestamp))));
    }
}
