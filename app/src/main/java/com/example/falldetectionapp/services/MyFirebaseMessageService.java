package com.example.falldetectionapp.services;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;

public class MyFirebaseMessageService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FCM Log", "token : " + token);
//        f9dD-3EDQlq_7q2GYMztCF:APA91bFNolsE_zMQkSKeSggoAdlvhTRifUNczr4DUV5Bt6Rsi-Kzn92d9tXI5hDBf72k_yqeJ_UC9rZCa8mtgT3z2YdCRKXHPuLc4mMBBTS4rkdxkLvQ2PLRuBaO49AETga9-lhfSUsW
    }
}
