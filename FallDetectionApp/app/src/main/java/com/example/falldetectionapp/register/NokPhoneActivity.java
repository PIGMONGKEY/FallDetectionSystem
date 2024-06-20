package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.SignUpDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.DTO.UserPhoneTokenDTO;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.StartActivity;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.firebase.messaging.FirebaseMessaging;
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

    private Button registerDoneButton;
    private ImageButton addNokphoneButton;
    private EditText nokPhoneEditText_1, nokPhoneEditText_2;

    private UserInfoDTO userInfoDTO;
    private String fcmDeviceToken;
    private boolean secondNokphone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_nokphone);

        init();
    }

//    초기 설정을 넣어주세요
    private void init() {
        getDataFromIntent();
        getFcmDeviceToken();
        setTitle("비상연락처 등록");
        setView();
        setListener();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        userInfoDTO = (UserInfoDTO) intent.getSerializableExtra("userInfo");
    }

    // FCM 기기 토큰 읽어오기
    private void getFcmDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            fcmDeviceToken = task.getResult();
        });
    }

    private void setView() {
        registerDoneButton = findViewById(R.id.finishButton_register);
        nokPhoneEditText_1 = findViewById(R.id.nokPhone_1_EditText_register);
        nokPhoneEditText_2 = findViewById(R.id.nokPhone_2_EditText_register);
        addNokphoneButton = findViewById(R.id.add_nokphone_button_register);
    }

//    리스너는 여기에 모아주세요
    private void setListener() {
        registerDoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nokphone_1;
                String nokphone_2;

                if (secondNokphone) {
                    nokphone_1 = nokPhoneEditText_1.getText().toString().trim();
                    nokphone_2 = nokPhoneEditText_2.getText().toString().trim();
                    if (inputCheck(nokphone_1, nokphone_2)) {
                        List<String> nokPhones = new ArrayList<>();
                        nokPhones.add(nokphone_1);
                        nokPhones.add(nokphone_2);

                        userInfoDTO.setNokPhones(nokPhones);

                        requestRegister();
                    } else {
                        Toast.makeText(getApplicationContext(), "보호자 연락처를 모두 입력해주세요.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    nokphone_1 = nokPhoneEditText_1.getText().toString().trim();
                    if (inputCheck(nokphone_1)) {
                        List<String> nokPhones = new ArrayList<>();
                        nokPhones.add(nokphone_1);

                        userInfoDTO.setNokPhones(nokPhones);

                        requestRegister();
                    } else {
                        Toast.makeText(getApplicationContext(), "최소 한 개 이상의 보호자 연락처가 필요합니다.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        addNokphoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secondNokphone = true;
                addNokphoneButton.setVisibility(View.GONE);
                nokPhoneEditText_2.setVisibility(View.VISIBLE);
            }
        });
    }

    private boolean inputCheck(String nokphone_1) {
        return !nokphone_1.isEmpty();
    }

    private boolean inputCheck(String nokphone_1, String nokphone_2) {
        return !nokphone_1.isEmpty() && !nokphone_2.isEmpty();
    }

    private void requestRegister() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        // 사용자 핸드폰 FCM 토큰 DTO 객체 생성
        UserPhoneTokenDTO userPhoneTokenDTO = new UserPhoneTokenDTO();
        userPhoneTokenDTO.setCameraId(userInfoDTO.getCameraId());
        userPhoneTokenDTO.setToken(fcmDeviceToken);

        // 회원가입 정보 DTO 객체 생성
        SignUpDTO signUpDTO = new SignUpDTO();
        signUpDTO.setUserInfo(userInfoDTO);
        signUpDTO.setPhoneToken(userPhoneTokenDTO);

        userService.signUp(signUpDTO).enqueue(new Callback<BasicResponseDTO>() {
            @Override
            public void onResponse(Call<BasicResponseDTO> call, Response<BasicResponseDTO> response) {
                if (response.isSuccessful()) {
                    showAlertDialog("회원가입", "회원가입이 완료되었습니다.");
                } else {
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());

                        showAlertDialog("회원가입", "회원가입에 실패했습니다." +
                                "\n오류 메시지 : " + basicResponseDTO.getMessage());

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

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(NokPhoneActivity.this, StartActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }
}