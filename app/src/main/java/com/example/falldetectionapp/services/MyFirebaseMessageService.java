package com.example.falldetectionapp.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.falldetectionapp.R;
import com.example.falldetectionapp.notification.FirebaseCloudMessageToken;
import com.example.falldetectionapp.notification.EmergencyAlertNotificationActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM Log", "token : " + token);
        FirebaseCloudMessageToken.setToken(token);
//        dIabE-11SbKI67SSQ9tdBd:APA91bHsAQKfT5fkaBPIMqP_POjDtf4AJpAsyIZUIs9pY9XjTknSq-hhqEVSzfEwylQ24GBz4necTzzoZ9qeL_oqpeDD2zxqr6iqox0-d6lk6AU95NfeFhrGP3l8st1_PysibmnDZB5J
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("FCM Log", "onMessageReceived");
        createNotificationChannel();
        showNotification(message.getData().get("title"), message.getData().get("body"));
    }

    private void showNotification(String title, String body) {
        Intent notiIntent = new Intent(getApplicationContext(), EmergencyAlertNotificationActivity.class);
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                          | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                                    getApplicationContext(),
                                0,
                                            notiIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "Detection_Channel")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_home)
                .setPriority(NotificationCompat.PRIORITY_HIGH | NotificationCompat.FLAG_HIGH_PRIORITY)
                .setFullScreenIntent(pendingIntent, true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("FCM Log", "NO PERMISSION");
            return;
        }
        notificationManagerCompat.notify(0, builder.build());
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Detection_Channel", "push",NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void turnScreenOn() {
        PowerManager pm = (PowerManager) getApplication().getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "FallDetection:detection");
        wakeLock.acquire();
        createNotificationChannel();
        showNotification("title", "body");
        wakeLock.release();
    }
}
