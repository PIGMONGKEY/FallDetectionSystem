package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.AuthTokenDTO;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.LoginDTO;
import com.example.falldetectionapp.register.RegisterActivity;
import com.example.falldetectionapp.retrofit.AuthService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * 로그인 화면 입니다.
 * activity_log_in.xml과 연결됩니다.
 */
// TODO: 비밀번호 찾기, 자동 로그인 구현
// TODO: 로그인 할 때, 기기 토큰 업데이트
public class LoginActivity extends AppCompatActivity {

    private Button registerButton, loginButton;
    private EditText cameraIdEditText, passwordEditText;
    private String fcmDeviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        init();
    }

    // 초기 설정을 넣어주세요
    private void init() {
        getFcmDeviceToken();
        setView();
        setListener();
    }

    // FCM 기기 토큰 읽어오기
    private void getFcmDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            fcmDeviceToken = task.getResult();
        });
    }

    private void setView() {
        registerButton = findViewById(R.id.registerButton_login);
        loginButton = findViewById(R.id.loginButton_login);
        cameraIdEditText = findViewById(R.id.cameraIdEditText_login);
        passwordEditText = findViewById(R.id.passwordEditText_login);
    }

    // 리스너는 여기 모아주세요
    private void setListener() {

        // 회원가입 버튼
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("fcmDeviceToken", fcmDeviceToken);
                startActivity(intent);
            }
        });

        // 로그인 버튼
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cameraId = cameraIdEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!cameraId.isEmpty() && !password.isEmpty()) {
//                    requestLogin(cameraId, password);

                    // UI 구현을 위한 인텐트 로직
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("personalToken", "token");
                    intent.putExtra("cameraId", cameraId);
                    startActivity(intent);
                } else if (cameraId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 서버에 아이디와 비밀번호를 보내서 로그인 요청한다.
     * 로그인 성공 시, token을 발급받고,
     * intent에 token과 cameraId를 넣어서 HomeActivity를 호출한다.
     */
    private void requestLogin(String cameraId, String password) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:10000/") // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        AuthService authService = retrofit.create(AuthService.class);
        authService.requestLogin(new LoginDTO(cameraId, password, fcmDeviceToken)).enqueue(new Callback<BasicResponseDTO<AuthTokenDTO>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<AuthTokenDTO>> call, Response<BasicResponseDTO<AuthTokenDTO>> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("personalToken", response.body().getData().getToken());
                    intent.putExtra("cameraId", cameraId);
                    startActivity(intent);
                } else {
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());
                        Toast.makeText(getApplicationContext(), basicResponseDTO.getMessage(), Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<AuthTokenDTO>> call, Throwable t) {
                Log.d("LOGIN", t.getMessage());
                Toast.makeText(getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}