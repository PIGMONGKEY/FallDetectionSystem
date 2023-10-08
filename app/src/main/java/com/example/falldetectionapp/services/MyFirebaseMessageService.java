package com.example.falldetectionapp.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM Log", "token : " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        Log.d("FCM Log", "Message received : " + message);
        super.onMessageReceived(message);
    }
}
