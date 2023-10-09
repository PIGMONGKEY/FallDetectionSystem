package com.example.falldetectionapp.services;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.falldetectionapp.LoginActivity;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.notification.ImportantActivity;
import com.example.falldetectionapp.notification.NotificationTestActivity;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM Log", "token : " + token);
//        360 : eLbK4HItS5aPXgNef4TyIs:APA91bF8oS_PZ6dWI7hivFRtKbe9u9wjJ3KR24FbZHLUquoFOQmOe3RXjV0w4l5z-0SovJyQ3auc_a-J1dKIiE6TzL25LljTQoBurpRd78UkR6ShrMdLPKeVX9CtSRegBgGrcbrqt301
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("FCM Log", "onMessageReceived");
        turnScreenOn();
        createNotificationChannel();
        showNotification(message.getNotification().getTitle(), message.getNotification().getBody());
    }

    private void showNotification(String title, String body) {
        Intent notiIntent = new Intent(this, ImportantActivity.class);
        notiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                          | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                                    this,
                                0,
                                            notiIntent,
                                            PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "Detection_Channel")
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.ic_home)
                .setPriority(NotificationCompat.PRIORITY_HIGH | NotificationCompat.FLAG_HIGH_PRIORITY)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
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
        try {
            Thread.sleep(3000);
        } catch (Exception e) {

        }
        wakeLock.release();
    }
}
