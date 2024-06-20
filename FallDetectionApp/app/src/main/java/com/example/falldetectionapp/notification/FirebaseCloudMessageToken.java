package com.example.falldetectionapp.notification;

public class FirebaseCloudMessageToken {
    private static String token = null;

    public static String getToken() {
        return token;
    }

    public static void setToken(String newToken) {
        token = newToken;
    }
}
