package com.example.falldetectionapp.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.example.falldetectionapp.R;

public class EmergencyAlertNotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alert_notification);
        Log.d("FCM Log", "onCreate------------------------------------------");
        turnScreenOnAndKeyguardOff();
    }

    // TODO: 소리 재생, 및 끄기 동작 구현 / 알림 끄면 서버에 알려주는 기능 구현

    private void turnScreenOnAndKeyguardOff() {
        Log.d("FCM Log", "turnScreenOnAndKeyguardOff------------------------------------------");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true);
            setTurnScreenOn(true);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                    WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        } else {
            getWindow().addFlags(
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                            WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            );
        }

        KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("TEST", "Keyguard disable");
            keyguardManager.requestDismissKeyguard(this, null);
        }
    }
}