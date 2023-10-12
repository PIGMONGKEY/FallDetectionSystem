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
import com.example.falldetectionapp.DTO.LoginDTO;
import com.example.falldetectionapp.register.RegisterActivity;
import com.example.falldetectionapp.retrofit.AuthService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 로그인 화면 입니다.
 * activity_log_in.xml과 연결됩니다.
 */
public class LoginActivity extends AppCompatActivity {

    private Button registerButton, loginButton;
    private EditText cameraIdEditText, passwordEditText;
    private CheckBox autoLoginCheckBox;
    private TextView forgetPasswordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        init();
    }

    // 초기 설정을 넣어주세요
    private void init() {
        setView();
        setListener();
    }

    private void setView() {
        registerButton = findViewById(R.id.registerButton_login);
        loginButton = findViewById(R.id.loginButton_login);
        cameraIdEditText = findViewById(R.id.cameraIdEditText_login);
        passwordEditText = findViewById(R.id.passwordEditText_login);
        autoLoginCheckBox = findViewById(R.id.autoLoginCheckBox_login);
        forgetPasswordTextView = findViewById(R.id.forgetPasswordTextView_login);
    }

    // 리스너는 여기 모아주세요
    private void setListener() {

        // 회원가입 버튼
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
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
                    requestLogin(cameraId, password);
                } else if (cameraId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 자동로그인 체크박스 버튼
        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        // TextView에도 onClickListener를 달 수 있습니다.
        forgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 서버에 아이디와 비밀번호를 보내서 로그인 요청한다.
     * 로그인 성공 시, token을 발급받고,
     * intent에 token과 cameraId를 넣어서 HomeActivity를 호출한다.
     * @param cameraId
     * @param password
     */
    private void requestLogin(String cameraId, String password) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:10000") // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        AuthService authService = retrofit.create(AuthService.class);
        authService.requestLogin(new LoginDTO(cameraId, password)).enqueue(new Callback<AuthTokenDTO>() {
            @Override
            public void onResponse(Call<AuthTokenDTO> call, Response<AuthTokenDTO> response) {
                if (response.body().getToken().trim().equals("fail")) {
                    Toast.makeText(getApplicationContext(), "올바르지 않은 아이디 혹은 비밀번호 입니다.", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("personalToken", response.body().getToken());
                    intent.putExtra("cameraId", cameraId);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<AuthTokenDTO> call, Throwable t) {
                Log.d("LOGIN", t.getMessage());
            }
        });
    }
}