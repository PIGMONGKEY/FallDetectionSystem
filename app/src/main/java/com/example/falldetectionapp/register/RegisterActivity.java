package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 회원가입 시, 카메라ID와 비밀번호를 입력하는 창입니다.
 * activity_sign_up.xml과 연결됩니다.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button toInfoButton;
    private EditText cameraIdEditText, passwordEditText, passwordCheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        init();
    }

//    초기 설정을 넣어주세요
    private void init() {
        setTitle("회원가입");
        setView();
        setListener();
    }

    private void setView() {
        toInfoButton = findViewById(R.id.continueToInfoButton);
        cameraIdEditText = findViewById(R.id.cameraIdEditText_register);
        passwordEditText = findViewById(R.id.passwordEditText_register);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText_register);
    }

//    리스너는 여기 모아주세요
    private void setListener() {
        toInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cameraId = cameraIdEditText.getText().toString().trim();

                // 존재하는 카메라 ID 인지 확인
                checkCameraId(cameraId);
            }
        });
    }

    /**
     * 서버로 요청을 보내서 존재하는 카메라 ID인지 확인한다.
     * @param cameraId
     */
    private void checkCameraId(String cameraId) {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
        .baseUrl("http://10.0.2.2:10000") // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
        .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
        .build();

        UserService userService = retrofit.create(UserService.class);

        userService.checkCameraId(cameraId).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("RETROFIT", "success : " + response.body());
                if (response.body().equals("exist")) {
                    // 있는 카메라 아이디
                    if (passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())) {

                        UserInfoDTO userInfoDTO = new UserInfoDTO();
                        userInfoDTO.setCameraId(cameraId);
                        userInfoDTO.setUserPassword(passwordEditText.getText().toString());

                        Intent intent = new Intent(RegisterActivity.this, InfoActivity.class);
                        intent.putExtra("userInfo", userInfoDTO);
                        startActivity(intent);

                    } else {
                        Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // 없는 카메라 아이디
                    Toast.makeText(getApplicationContext(), "존재하지 않는 카메라 ID 입니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("RETROFIT", t.getCause().toString());
            }
        });
    }
}