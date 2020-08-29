package com.example.bachelorthesisclient.util;

import com.example.bachelorthesisclient.model.EventDetails;
import com.example.bachelorthesisclient.wrapper.GsonWrapper;
import com.example.bachelorthesisclient.wrapper.SharedPreferenceWrapper;

public class EventDetailsPersistenceUtil {
    private static final String EVENT_DETAILS_KEY = "EVENT_DETAILS_KEY";

    public static void putEventDetails(EventDetails eventDetails) {
        String json = GsonWrapper.toJson(eventDetails);
        SharedPreferenceWrapper.getInstance().put(EVENT_DETAILS_KEY, json);
    }

    public static EventDetails getEventDetails() {
        String json = (String) SharedPreferenceWrapper.getInstance().get(EVENT_DETAILS_KEY, "");

        if (json.equals("")) {
            return null;
        } else {
            return (EventDetails) GsonWrapper.fromJson(json, EventDetails.class);
        }
    }

    public static void clear() {
        SharedPreferenceWrapper.getInstance().remove(EVENT_DETAILS_KEY);
    }
}
