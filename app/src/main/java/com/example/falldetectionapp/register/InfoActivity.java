package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.R;

/**
 * 회원가입 시, 사용자 정보를 입력하는 창입니다.
 * activity_info.xml과 연결됩니다.
 */
public class InfoActivity extends AppCompatActivity {

    private Button toAddressButton, phoneAuthButton;
    private EditText nameEditText, phoneEditText, phoneCheckEditText;
    private UserInfoDTO userInfoDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }

//    초기설정을 넣어주세요
    private void init() {
        getDataFromIntent();
        setTitle("개인정보");
        setView();
        setListener();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        userInfoDTO = (UserInfoDTO) intent.getSerializableExtra("userInfo");
    }

    private void setView() {
        toAddressButton = findViewById(R.id.continueToAddressButton);
        phoneAuthButton = findViewById(R.id.phoneAuthButton_register);
        nameEditText = findViewById(R.id.nameEditText_register);
        phoneEditText = findViewById(R.id.phoneEditText_register);
        phoneCheckEditText = findViewById(R.id.phoneAuthEditText_register);
    }

//    리스너는 여기에 모아주세요
    private void setListener() {
        // 다음 버튼
        toAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });

        // 전화번호 인증번호 전송 버튼 리스너
        phoneAuthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}