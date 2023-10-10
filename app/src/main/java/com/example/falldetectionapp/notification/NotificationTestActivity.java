package com.example.falldetectionapp.notification;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import android.app.KeyguardManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.falldetectionapp.R;

public class NotificationTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);

        Log.d("FCM Log", "onCreate--Main------------------------------------------");

        findViewById(R.id.button_noti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sleep(6000);
//                    turnScreenOnAndKeyguardOff();
                    turnScreenOn();
                    Log.d("TEST", "Screen Turn ON ");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


    private void turnScreenOnAndKeyguardOff() {
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

    private void turnScreenOn() {
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "FallDetection:detection");
        wakeLock.acquire();
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            Log.d("FCM Log", "error");
        }
        wakeLock.release();
    }
}