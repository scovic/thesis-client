package com.example.bachelorthesisclient.model.mapobject;

import com.example.bachelorthesisclient.R;

import org.osmdroid.util.GeoPoint;

public class ExitMapObject extends MapObject {
    public ExitMapObject(int id, String name, GeoPoint location) {
        super(id, name, location, R.drawable.icon_map_emergency_exit);
    }
}
