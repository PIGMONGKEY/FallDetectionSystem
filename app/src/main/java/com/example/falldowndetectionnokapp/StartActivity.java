package com.example.falldowndetectionnokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.falldowndetectionnokapp.DTO.BasicResponseDTO;
import com.example.falldowndetectionnokapp.DTO.NokPhoneRegisterDTO;
import com.example.falldowndetectionnokapp.retrofit.HttpService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StartActivity extends AppCompatActivity {

    private Button confirmButton;
    private EditText phoneET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        init();
    }

    private void init() {
        setViews();
        setListeners();
        checkAutoLogin();
    }

    private void setViews() {
        confirmButton = findViewById(R.id.confirmButton_start);
        phoneET = findViewById(R.id.phoneEditText_start);
    }

    private void setListeners() {
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = phoneET.getText().toString().trim();

                getAlertDialog()
                        .setMessage("입력하신 전화번호는 " + phone + " 입니다.\n" +
                                "이후 입력하신 번호 변경을 위해서는 앱을 다시 설치하여야 합니다.\n" +
                                "올바르게 입력하셨다면, \"확인\"을 눌러주세요.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestRegisterNokPhoneToken();
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }

    private void checkAutoLogin() {
        if (!getSharedPreferences().isEmpty()) {
            // TODO: 홈 만들어서 거기로 이동
        }
    }

    private AlertDialog.Builder getAlertDialog() {
        return new AlertDialog.Builder(this);
    }

    private void requestRegisterNokPhoneToken() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        HttpService httpService = retrofit.create(HttpService.class);

        NokPhoneRegisterDTO nokPhoneRegisterDTO = new NokPhoneRegisterDTO(phoneET.getText().toString().trim(), "tokentoken");

        httpService.requestRegisterToken(nokPhoneRegisterDTO).enqueue(new Callback<BasicResponseDTO>() {
            @Override
            public void onResponse(Call<BasicResponseDTO> call, Response<BasicResponseDTO> response) {
                if (response.isSuccessful()) {
                    getAlertDialog()
                            .setMessage("성공했습니다. 감사합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO: 홈으로 이동
                                }
                            })
                            .show();
                } else {
                    getAlertDialog()
                            .setMessage("실패했습니다. 죄송합니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO> call, Throwable t) {
                Log.d("HTTP", t.getMessage());
            }
        });
    }

    /**
     * SharedPreferences에서 자동로그인 정보를 가져옴
     * @return HashMap 형태로 로그인 정보를 반환함
     */
    private String getSharedPreferences() {
        SharedPreferences sp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        return sp.getString("phone", null);
    }
}