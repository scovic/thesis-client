package com.example.bachelorthesisclient.repository;

import com.example.bachelorthesisclient.repository.eventdetails.EventDetailsRepository;
import com.example.bachelorthesisclient.repository.eventdetails.EventDetailsRepositoryImpl;
import com.example.bachelorthesisclient.repository.feed.FeedRepositoryImpl;
import com.example.bachelorthesisclient.repository.iam.IamRepositoryImpl;
import com.example.bachelorthesisclient.repository.notification.NotificationRepositoryImpl;
import com.example.bachelorthesisclient.repository.settings.SettingsRepositoryImpl;
import com.example.bachelorthesisclient.repository.ticket.TicketRepositoryImpl;

public class RepositoryFactory {
    public static final String FEED_REPOSITORY = "FeedRepository";
    public static final String NOTIFICATION_REPOSITORY = "NotificationRepository";
    public static final String SETTINGS_REPOSITORY = "SettingsRepository";
    public static final String IAM_REPOSITORY = "IamRepository";
    public static final String TICKET_REPOSITORY = "TicketRepository";
    public static final String EVENT_DETAILS_REPOSITORY = "EventDetailsRepository";

    public static Object get(String name) {
        switch (name) {
            case FEED_REPOSITORY:
                return FeedRepositoryImpl.getInstance();
            case NOTIFICATION_REPOSITORY:
                return NotificationRepositoryImpl.getInstance();
            case SETTINGS_REPOSITORY:
                return SettingsRepositoryImpl.getInstance();
            case IAM_REPOSITORY:
                return IamRepositoryImpl.getInstance();
            case TICKET_REPOSITORY:
                return TicketRepositoryImpl.getInstance();
            case EVENT_DETAILS_REPOSITORY:
                return EventDetailsRepositoryImpl.getInstance();
            default:
                return null;
        }
    }
}
