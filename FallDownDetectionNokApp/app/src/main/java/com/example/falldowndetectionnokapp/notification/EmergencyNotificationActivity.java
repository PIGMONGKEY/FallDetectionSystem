package com.example.falldowndetectionnokapp.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.example.falldowndetectionnokapp.R;

public class EmergencyNotificationActivity extends AppCompatActivity {

    private Button releaseAlertButton;
    private boolean soundStopFlag = false;
    private SoundThread soundThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_notification);

        init();
    }

    private void init() {
        turnScreenOnAndKeyguardOff();
        setVolMax();
        setViews();
        setListeners();
        startSound();
    }

    private void setViews() {
        releaseAlertButton = findViewById(R.id.release_button);
    }

    private void setListeners() {
        releaseAlertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                soundStopFlag = true;
                moveTaskToBack(true);
                finishAndRemoveTask();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
    }

    // 화면을 On
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
            Log.d("FallDownDetection", "Keyguard disable");
            keyguardManager.requestDismissKeyguard(this, null);
        }
    }

    // 알림 울릴 때 소리 최대로 키움
    private void setVolMax() {
        AudioManager audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 15, 0);
    }

    private void startSound() {
        soundThread = new SoundThread();
        soundThread.start();
    }

    private class SoundThread extends Thread {
        @Override
        public void run() {
            super.run();

            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.emergency_alert);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            while (mediaPlayer.isPlaying()) {
                if (soundStopFlag) {
                    mediaPlayer.stop();
                }
            }

            mediaPlayer.release();
        }
    }
}