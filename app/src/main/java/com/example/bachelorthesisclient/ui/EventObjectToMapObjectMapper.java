package com.example.bachelorthesisclient.ui;

import com.example.bachelorthesisclient.model.EventObject;
import com.example.bachelorthesisclient.model.mapobject.ExitMapObject;
import com.example.bachelorthesisclient.model.mapobject.MapObject;
import com.example.bachelorthesisclient.model.mapobject.StageMapObject;
import com.example.bachelorthesisclient.model.mapobject.WcMapObject;

import org.osmdroid.util.GeoPoint;

public class EventObjectToMapObjectMapper {
    public static MapObject map(EventObject eventObject) {
        if (eventObject.isStage()) {
            return new StageMapObject(
                    eventObject.getId(),
                    eventObject.getName(),
                    new GeoPoint(eventObject.getLatitude(), eventObject.getLongitude())
            );
        } else if (eventObject.isExit()) {
            return new ExitMapObject(
                    eventObject.getId(),
                    eventObject.getName(),
                    new GeoPoint(eventObject.getLatitude(), eventObject.getLongitude())
            );
        } else if (eventObject.isWc()) {
            return new WcMapObject(
                    eventObject.getId(),
                    eventObject.getName(),
                    new GeoPoint(eventObject.getLatitude(), eventObject.getLongitude())
            );
        } else {
            return null;
        }
    }
}
