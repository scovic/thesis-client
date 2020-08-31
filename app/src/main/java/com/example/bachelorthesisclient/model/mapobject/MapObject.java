package com.example.bachelorthesisclient.model.mapobject;

import org.osmdroid.util.GeoPoint;

public class MapObject {
    private int id;
    private String name;
    private GeoPoint location;
    private int drawableId;

    protected MapObject(int id, String name, GeoPoint location, int drawableId) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.drawableId = drawableId;
    }

    public MapObject(String name, GeoPoint location, int drawableId) {
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

    public int getId() {
        return id;
    }
}
