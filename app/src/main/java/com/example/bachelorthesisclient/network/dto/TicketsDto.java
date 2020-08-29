package com.example.bachelorthesisclient.network.dto;

import java.util.List;

public class TicketsDto {
    private List<TicketDto> list;

    public TicketsDto(List<TicketDto> list) {
        this.list = list;
    }

    public List<TicketDto> getList() {
        return list;
    }

    public void setList(List<TicketDto> list) {
        this.list = list;
    }
}
