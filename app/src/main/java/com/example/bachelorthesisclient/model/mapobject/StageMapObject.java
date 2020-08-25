package com.example.bachelorthesisclient.model.mapobject;

import com.example.bachelorthesisclient.R;

import org.osmdroid.util.GeoPoint;

public class StageMapObject extends MapObject {
    public StageMapObject(String name, GeoPoint location) {
        super(name, location, R.drawable.icon_map_stage);
    }
}
