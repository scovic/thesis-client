package com.example.bachelorthesisclient.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {
    public static String formatDate(Date date) {
        return new SimpleDateFormat("dd.MM.yyyy - HH:mm").format(date);
    }
}
