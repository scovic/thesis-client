package com.example.bachelorthesisclient.model;

import org.osmdroid.util.GeoPoint;

import java.util.Date;

public class EventDetails {
    private GeoPoint location;
    private String name;
    private Date eventDate;

    public EventDetails(GeoPoint location, String name, Date eventDate) {
        this.location = location;
        this.name = name;
        this.eventDate = eventDate;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Date getEventDate() {
        return eventDate;
    }
}
