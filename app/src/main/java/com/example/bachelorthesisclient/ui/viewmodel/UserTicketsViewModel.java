package com.example.bachelorthesisclient.ui.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bachelorthesisclient.model.Ticket;
import com.example.bachelorthesisclient.repository.RepositoryFactory;
import com.example.bachelorthesisclient.repository.ticket.TicketRepository;
import com.example.bachelorthesisclient.util.LoggedInUserPersistenceUtil;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

public class UserTicketsViewModel extends ViewModel {
    private final int maxTicketsSize = 10;

    private TicketRepository ticketRepository;
    private MutableLiveData<List<Ticket>> tickets;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<Integer> ticketsQuantity;
    private MutableLiveData<Boolean> reserveBtnDisabled;
    private MutableLiveData<Boolean> showErrorMessage;
    private MutableLiveData<Boolean> noReservedTickets;

    public UserTicketsViewModel() {
        this.ticketRepository = (TicketRepository) RepositoryFactory.get(RepositoryFactory.TICKET_REPOSITORY);

        List<Ticket> startList = new ArrayList<>();
        this.tickets = new MutableLiveData<>(startList);
        this.loading = new MutableLiveData<>(true);
        this.ticketsQuantity = new MutableLiveData<>(-1);
        this.reserveBtnDisabled = new MutableLiveData<>(false);
        this.showErrorMessage = new MutableLiveData<>(false);
        this.noReservedTickets = new MutableLiveData<>(true);

        reloadData();
    }

    public void reloadData() {
        this.ticketRepository.getUserTickets(LoggedInUserPersistenceUtil.getUserId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new SingleObserver<List<Ticket>>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onSuccess(List<Ticket> tickets) {
                                setTickets(tickets);
                                setLoading(false);

                                if (tickets.size() >= maxTicketsSize) {
                                    setReserveBtnDisabled(true);
                                    setShowErrorMessage(true);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }
                        }
                );
    }

    public void purchaseTickets() {
        if (ticketsQuantity.getValue() > 0 || ticketsQuantity.getValue() <= maxTicketsSize - tickets.getValue().size()) {
            setLoading(true);
            this.ticketRepository.purchaseTickets(
                    LoggedInUserPersistenceUtil.getUserId(),
                    ticketsQuantity.getValue()
            )
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            new SingleObserver<List<Ticket>>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onSuccess(List<Ticket> tickets) {
                                    List<Ticket> ticketList = getTickets().getValue();
                                    ticketList.addAll(tickets);
                                    setTickets(ticketList);
                                    setLoading(false);
                                }

                                @Override
                                public void onError(Throwable e) {

                                }
                            }
                    );
        }
    }

    public MutableLiveData<List<Ticket>> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        if (tickets.size() == 0) {
            setNoReservedTickets(true);
        } else {
            setNoReservedTickets(false);
        }

        if (tickets.size() < maxTicketsSize) {
            setShowErrorMessage(false);
        }

        this.tickets.setValue(tickets);
    }

    public MutableLiveData<Boolean> getLoading() {
        return loading;
    }

    public void setLoading(Boolean loading) {
        this.loading.setValue(loading);
    }

    public MutableLiveData<Boolean> getReserveBtnDisabled() {
        return reserveBtnDisabled;
    }

    public void setReserveBtnDisabled(Boolean reserveBtnDisabled) {
        this.reserveBtnDisabled.setValue(reserveBtnDisabled);
    }

    public void setTicketsQuantity(Integer ticketsQuantity) {
        if (ticketsQuantity == 0) {
            setReserveBtnDisabled(true);
        } else if (ticketsQuantity > maxTicketsSize - tickets.getValue().size()) {
            setShowErrorMessage(true);
            setReserveBtnDisabled(true);
        } else {
            this.ticketsQuantity.setValue(ticketsQuantity);
            setShowErrorMessage(false);
            setReserveBtnDisabled(false);
        }
    }

    public MutableLiveData<Boolean> getShowErrorMessage() {
        return showErrorMessage;
    }

    public void setShowErrorMessage(Boolean showErrorMessage) {
        this.showErrorMessage.setValue(showErrorMessage);
    }

    public void removeTicket(Ticket ticket) {
        List<Ticket> ticketList = getTickets().getValue();
        ticketList.remove(ticket);
        setTickets(ticketList);
    }

    public MutableLiveData<Boolean> getNoReservedTickets() {
        return noReservedTickets;
    }

    public void setNoReservedTickets(Boolean noReservedTickets) {
        this.noReservedTickets.setValue(noReservedTickets);
    }
}
