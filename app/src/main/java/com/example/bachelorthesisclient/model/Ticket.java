package com.example.bachelorthesisclient.model;

import androidx.annotation.Nullable;

import com.example.bachelorthesisclient.network.dto.TicketDto;

import java.util.Date;

public class Ticket {
    private int id;
    private int buyerId;
    private Date dateOfPurchase;

    public Ticket(TicketDto ticketDto) {
        this.id = ticketDto.getId();
        this.buyerId = ticketDto.getUserId();
        this.dateOfPurchase = new Date(ticketDto.getPurchaseDate());
    }

    public Ticket(int id, int buyerId, Date dateOfPurchase) {
        this.id = id;
        this.buyerId = buyerId;
        this.dateOfPurchase = dateOfPurchase;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) {
            return true;
        }

        return id == ((Ticket) obj).getId();
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public int getId() {
        return id;
    }
}
