package com.example.bachelorthesisclient.repository.ticket;

import com.example.bachelorthesisclient.model.Ticket;

import java.util.List;

import io.reactivex.Single;

public interface TicketRepository {
    Single<List<Ticket>> purchaseTickets(int userId, int quantity);
    Single<List<Ticket>> getUserTickets(int userId);
    Single<Boolean> cancelTicket(int ticketId);
}
