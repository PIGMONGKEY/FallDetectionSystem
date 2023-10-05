package com.example.falldetectionapp.notification;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;
import android.app.KeyguardManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class NotificationTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_test);

        findViewById(R.id.button_noti).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sleep(6000);
                    turnScreenOnAndKeyguardOff();
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
            keyguardManager.requestDismissKeyguard(this, null);
        }
    }
}