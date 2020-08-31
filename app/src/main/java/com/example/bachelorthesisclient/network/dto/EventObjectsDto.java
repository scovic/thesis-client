package com.example.bachelorthesisclient.network.dto;

import java.util.List;

public class EventObjectsDto {
    List<EventObjectDto> eventObjects;

    public EventObjectsDto(List<EventObjectDto> eventObjects) {
        this.eventObjects = eventObjects;
    }

    public List<EventObjectDto> getEventObjects() {
        return eventObjects;
    }

    public void setEventObjects(List<EventObjectDto> eventObjects) {
        this.eventObjects = eventObjects;
    }
}
