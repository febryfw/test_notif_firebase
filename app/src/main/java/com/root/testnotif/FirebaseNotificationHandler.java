package com.root.testnotif;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationHandler extends FirebaseMessagingService{

    private final static String TAG = "NOTIFICATION";
    private String message, title;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "onMessageReceived: " + remoteMessage.getData().toString());

        if (remoteMessage.getData() != null){
            Map<String, String> notifData = new HashMap<>(remoteMessage.getData());
            message = notifData.get("message").trim();
            title = notifData.get("title").trim();

            sendNotif();

        } else {
            Log.e(TAG, "Data null");
        }
    }

    private void sendNotif(){
        String channelId = "skor-channel";
        String channelName = "SKOR Channel";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder notifBuilder = new NotificationCompat.Builder(getApplicationContext(), channelId)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true);

        NotificationManagerCompat notifManager = NotificationManagerCompat.from(getApplicationContext());
        notifManager.notify(100, notifBuilder.build());
    }
}
