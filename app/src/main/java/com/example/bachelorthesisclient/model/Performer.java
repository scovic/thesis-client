package com.example.bachelorthesisclient.model;

import com.example.bachelorthesisclient.network.dto.PerformerDto;

import java.util.Date;

public class Performer {
    private int id;
    private int stageId;
    private String name;
    private Date startTime;

    public Performer(PerformerDto performerDto) {
        this.id = performerDto.getId();
        this.stageId = performerDto.getStageId();
        this.name = performerDto.getName();
        this.startTime = new Date(performerDto.getStartTime());
    }


    public Performer(int id, int stageId, String name, Date startTime) {
        this.id = id;
        this.stageId = stageId;
        this.name = name;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public int getStageId() {
        return stageId;
    }

    public String getName() {
        return name;
    }

    public Date getStartTime() {
        return startTime;
    }
}
