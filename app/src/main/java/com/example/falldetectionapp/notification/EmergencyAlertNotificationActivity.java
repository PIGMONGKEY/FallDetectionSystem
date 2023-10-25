package com.example.falldetectionapp.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.KeyguardManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.EmergencyService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EmergencyAlertNotificationActivity extends AppCompatActivity {

    private String fcmDeviceToken;
    private TimerThread timerThread;
    private SoundThread soundThread;
    private Button sosButton, releaseButton;
    private TextView alertTimerTextView;
    private boolean timerStopFlag = false;
    private boolean emergencyFlag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_alert_notification);

        turnScreenOnAndKeyguardOff();
        init();
        startTimer();
        startSound();
    }

    private void init() {
        setView();
        setListener();
        getFcmDeviceToken();
    }

    private void setView() {
        sosButton = findViewById(R.id.SOS_button);
        releaseButton = findViewById(R.id.release_button);
        alertTimerTextView = findViewById(R.id.alertTimerTextView);
    }

    private void setListener() {

        // 구조 버튼을 누르면 즉시 요청함
        sosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerStopFlag = true;
                requestSOS();
            }
        });

        // 중단 버튼을 누르면 중단 요청함
        releaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timerStopFlag = true;
                emergencyFlag = false;
                requestReleaseEmergency();
            }
        });
    }

    // 핸드폰 토큰을 읽어옴
    // 핸드폰 토큰으로 cameraId를 조회하기 위함
    private void getFcmDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            fcmDeviceToken = task.getResult();
        });
    }

    // TODO: 소리 재생, 및 끄기 동작 구현 / 알림 끄면 서버에 알려주는 기능 구현

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

    // 3분 타이머 시작
    private void startTimer() {
        timerThread = new TimerThread();
        timerThread.start();
    }

    private void startSound() {
        soundThread = new SoundThread();
        soundThread.start();
    }

    // 긴급상황 해제
    private void requestReleaseEmergency() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        EmergencyService emergencyService = retrofit.create(EmergencyService.class);

        emergencyService.releaseEmergency(fcmDeviceToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("EMERGENCY", "requested");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                alertTimerTextView.setText("failed");
                Log.d("EMERGENCY", "failed");
            }
        });
    }

    // 구조 요청 보내기
    private void requestSOS() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        EmergencyService emergencyService = retrofit.create(EmergencyService.class);

        emergencyService.sos(fcmDeviceToken).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("EMERGENCY", "requested");
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                alertTimerTextView.setText("failed");
                Log.d("EMERGENCY", "failed");
            }
        });
    }

    /**
     * 3분 타미어 쓰레드 클래스
     * 3분 타이머가 끝나면 구조 요청을 보낸다.
     */
    private class TimerThread extends Thread {
        @Override
        public void run() {
            int second = 180;

            while (second > 0 && !timerStopFlag) {
                SystemClock.sleep(980);
                alertTimerTextView.setText(((int) second / 60) + ":" + ((int) second % 60));
                second--;
            }

            if (emergencyFlag) {
                requestSOS();
            }
        }
    }

    private class SoundThread extends Thread {
        @Override
        public void run() {
            super.run();

            MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.emergency_alert);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();

            while (mediaPlayer.isPlaying()) {
                if (timerStopFlag) {
                    if (!emergencyFlag) {
                        mediaPlayer.stop();
                    }
                }
            }

            mediaPlayer.release();
        }
    }
}