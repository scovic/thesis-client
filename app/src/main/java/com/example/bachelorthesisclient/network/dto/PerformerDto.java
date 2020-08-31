package com.example.bachelorthesisclient.network.dto;

public class PerformerDto {
    private int id;
    private int stageId;
    private String name;
    private long startTime;

    public PerformerDto(int id, int stageId, String name, long startTime) {
        this.id = id;
        this.stageId = stageId;
        this.name = name;
        this.startTime = startTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStageId() {
        return stageId;
    }

    public void setStageId(int stageId) {
        this.stageId = stageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
