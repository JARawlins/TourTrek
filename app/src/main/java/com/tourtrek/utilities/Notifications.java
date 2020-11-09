package com.tourtrek.utilities;

import android.content.Context;

import com.tourtrek.data.Tour;

import androidx.core.app.NotificationManagerCompat;

public class Notifications {


    private static void sendStartNotification(Context context, Tour tour) {
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
    }
}
