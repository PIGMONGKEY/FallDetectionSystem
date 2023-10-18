package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.SignUpDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.DTO.UserPhoneTokenDTO;
import com.example.falldetectionapp.HomeActivity;
import com.example.falldetectionapp.LoginActivity;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 회원가입 시, 보호자 핸드폰 번호를 입력하는 창입니다
 * activity_emergency_phone.xml과 연결됩니다.
 */
public class NokPhoneActivity extends AppCompatActivity {

    private Button registerDone;
    private EditText nokPhoneEditText_1, nokPhoneEditText_2;

    private UserInfoDTO userInfoDTO;
    private String fcmDeviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nokphone);

        init();
    }

//    초기 설정을 넣어주세요
    private void init() {
        getDataFromIntent();
        setTitle("비상연락처 등록");
        setView();
        setListener();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        userInfoDTO = (UserInfoDTO) intent.getSerializableExtra("userInfo");
        fcmDeviceToken = intent.getStringExtra("fcmDeviceToken");
    }

    private void setView() {
        registerDone = findViewById(R.id.finishButton_register);
        nokPhoneEditText_1 = findViewById(R.id.nokPhone_1_EditText_register);
        nokPhoneEditText_2 = findViewById(R.id.nokPhone_2_EditText_register);
    }

//    리스너는 여기에 모아주세요
    private void setListener() {
        registerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nokPhoneEditText_1.getText().toString().isEmpty() && nokPhoneEditText_2.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "최소 한 개 이상의 보호자 연락처가 필요합니다.", Toast.LENGTH_LONG).show();
                } else {
                    List<String> nokPhones = new ArrayList<>();
                    if (!nokPhoneEditText_1.getText().toString().isEmpty()) {
                        nokPhones.add(nokPhoneEditText_1.getText().toString());
                    }
                    if (!nokPhoneEditText_2.getText().toString().isEmpty()) {
                        nokPhones.add(nokPhoneEditText_2.getText().toString());
                    }

                    userInfoDTO.setNokPhones(nokPhones);

//                    requestRegister();

                    // UI 구현을 위한 임시 인텐트 로직
                    Intent intent = new Intent(NokPhoneActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void requestRegister() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:10000/") // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        UserPhoneTokenDTO userPhoneTokenDTO = new UserPhoneTokenDTO();
        userPhoneTokenDTO.setCameraId(userInfoDTO.getCameraId());
        userPhoneTokenDTO.setToken(fcmDeviceToken);

        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUserInfo(userInfoDTO);
        signUpDTO.setPhoneToken(userPhoneTokenDTO);

        userService.signUp(signUpDTO).enqueue(new Callback<BasicResponseDTO>() {
            @Override
            public void onResponse(Call<BasicResponseDTO> call, Response<BasicResponseDTO> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(NokPhoneActivity.this, LoginActivity.class);
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
            public void onFailure(Call<BasicResponseDTO> call, Throwable t) {
                Log.d("REGISTER", t.getCause().toString());
                Toast.makeText(getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}