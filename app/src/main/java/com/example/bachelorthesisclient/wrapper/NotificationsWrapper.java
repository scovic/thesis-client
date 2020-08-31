package com.example.bachelorthesisclient.wrapper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bachelorthesisclient.R;
import com.example.bachelorthesisclient.util.AppContext;
import com.example.bachelorthesisclient.util.RandomNumberUtil;

public class NotificationsWrapper {
    private static NotificationsWrapper instance;
    private NotificationCompat.Builder builder;

    public static NotificationsWrapper getInstance() {
        if (instance == null) {
            instance = new NotificationsWrapper();
        }

        return instance;
    }

    private final String CHANNEL_ID = "Notifications Channel";

    private NotificationsWrapper() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications";
            String description = "Notification channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this

            NotificationManager notificationManager = AppContext.getAppContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public NotificationsWrapper setTitle(String title) {
        this.checkForBuilder();
        builder.setContentTitle(title);
        return this;
    }

    public NotificationsWrapper setContent(String content) {
        this.checkForBuilder();
        builder.setContentText(content);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(content));

        return this;
    }

    public NotificationsWrapper setOnTapAction(Intent intent) {
        this.checkForBuilder();

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(AppContext.getAppContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        return this;
    }

    public NotificationsWrapper setIcon(int drawableId) {
        checkForBuilder();
        builder.setSmallIcon(drawableId);

        return this;
    }

    public NotificationsWrapper setAutoCancel() {
        checkForBuilder();
        builder.setAutoCancel(true);

        return this;
    }

    public void createNotification() {
        checkForBuilder();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(AppContext.getAppContext());
        int notificationId = RandomNumberUtil.getInstance().getRandomNumber();

        notificationManager.notify(notificationId, builder.build());
        builder = null;
    }

    private void checkForBuilder() {
        if (builder == null) {
            builder = new NotificationCompat.Builder(AppContext.getAppContext(), CHANNEL_ID);
            builder.setSmallIcon(R.drawable.ic_launcher_foreground);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
    }
}
