package com.example.bachelorthesisclient.repository.ticket;

import com.example.bachelorthesisclient.model.Ticket;
import com.example.bachelorthesisclient.network.api.TicketApi;
import com.example.bachelorthesisclient.network.dto.TicketDto;
import com.example.bachelorthesisclient.network.dto.TicketsDto;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;
import com.example.bachelorthesisclient.wrapper.RetrofitWrapper;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class TicketRepositoryImpl implements TicketRepository {
    private TicketApi mTicketApi;

    private static TicketRepository instance;

    public static TicketRepository getInstance() {
        if (instance == null) {
            instance = new TicketRepositoryImpl();
        }

        return instance;
    }

    private TicketRepositoryImpl() {
        this.mTicketApi = RetrofitWrapper.getInstance().create(TicketApi.class);
    }

    @Override
    public Single<List<Ticket>> purchaseTickets(int userId, int quantity) {
        return this.mTicketApi.purchaseTickets(
                getAuthorizationHeaderValue(),
                userId,
                quantity
        )
                .subscribeOn(Schedulers.io())
                .map(mapTicketsDtoToTickets());
    }

    @Override
    public Single<List<Ticket>> getUserTickets(int userId) {
        return this.mTicketApi.getUserTickets(
                getAuthorizationHeaderValue(),
                userId
        )
                .subscribeOn(Schedulers.io())
                .map(mapTicketsDtoToTickets());
    }

    @Override
    public Single<Boolean> cancelTicket(int ticketId) {
        return this.mTicketApi.cancelReservation(
                getAuthorizationHeaderValue(),
                ticketId
        )
                .subscribeOn(Schedulers.io());
    }

    private Function<TicketsDto, List<Ticket>> mapTicketsDtoToTickets() {
        return new Function<TicketsDto, List<Ticket>>() {
            @Override
            public List<Ticket> apply(TicketsDto ticketsDto) throws Exception {
                List<Ticket> tickets = new ArrayList<>();

                for (TicketDto ticketDto : ticketsDto.getList()) {
                    tickets.add(new Ticket(ticketDto));
                }

                return tickets;
            }
        };
    }

    private String getAuthorizationHeaderValue() {
        String token = LoggedInUserPersistenceUtil.getToken();
        return String.format("Bearer %s", token);
    }
}
