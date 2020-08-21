package com.example.bachelorthesisclient.model;

public class Location {
    private double Latitude;
    private double Longitude;

    public Location(double latitude, double longitude) {
        Latitude = latitude;
        Longitude = longitude;
    }

    public double getLatitude() {
        return Latitude;
    }

    public double getLongitude() {
        return Longitude;
    }
}
