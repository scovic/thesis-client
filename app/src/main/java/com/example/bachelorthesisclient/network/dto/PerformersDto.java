package com.example.bachelorthesisclient.network.dto;

import java.util.List;

public class PerformersDto {
    List<PerformerDto> performerDtoList;

    public PerformersDto(List<PerformerDto> performerDtoList) {
        this.performerDtoList = performerDtoList;
    }

    public List<PerformerDto> getPerformerDtoList() {
        return performerDtoList;
    }

    public void setPerformerDtoList(List<PerformerDto> performerDtoList) {
        this.performerDtoList = performerDtoList;
    }
}
