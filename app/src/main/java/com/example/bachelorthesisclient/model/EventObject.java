package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.network.dto.EventObjectDto;

public class EventObject {
    private final String STAGE_TYPE = "STAGE";
    private final String EXIT_TYPE = "EXIT";
    private final String WC_TYPE = "WC";

    private int id;
    private String name;
    private String type;
    private double latitude;
    private double longitude;

    public EventObject(EventObjectDto eventObjectDto) {
        this.id = eventObjectDto.getId();
        this.name = eventObjectDto.getName();
        this.type = eventObjectDto.getType();
        this.latitude = eventObjectDto.getLatitude();
        this.longitude = eventObjectDto.getLongitude();
    }

    public EventObject(int id, String name, String type, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean isStage() {
        return this.type.equals(STAGE_TYPE);
    }

    public boolean isExit() {
        return this.type.equals(EXIT_TYPE);
    }

    public boolean isWc() {
        return this.type.equals(WC_TYPE);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
