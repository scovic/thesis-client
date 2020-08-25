package com.example.bachelorthesisclient.model.mapobject;

import org.osmdroid.util.GeoPoint;

public class MapObject {
    protected String name;
    protected GeoPoint location;
    private int drawableId;

    protected MapObject(String name, GeoPoint location, int drawableId) {
        this.name = name;
        this.location = location;
        this.drawableId = drawableId;
    }

    public String getName() {
        return name;
    }

    public GeoPoint getLocation() {
        return location;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
