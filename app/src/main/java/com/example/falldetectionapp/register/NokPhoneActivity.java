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
import com.example.falldetectionapp.HomeActivity;
import com.example.falldetectionapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 회원가입 시, 보호자 핸드폰 번호를 입력하는 창입니다
 * activity_emergency_phone.xml과 연결됩니다.
 */
public class NokPhoneActivity extends AppCompatActivity {

    private Button registerDone;
    private EditText nokPhoneEditText_1, nokPhoneEditText_2;

    private UserInfoDTO userInfoDTO;

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
                        nokPhones.add(nokPhoneEditText_2.getText().toString());
                    }
                    if (!nokPhoneEditText_2.getText().toString().isEmpty()) {
                        nokPhones.add(nokPhoneEditText_2.getText().toString());
                    }

                    userInfoDTO.setNokPhones(nokPhones);

                    Log.d("REGISTER", userInfoDTO.toString());

//                    Intent intent = new Intent(NokPhoneActivity.this, HomeActivity.class);
//                    intent.putExtra("userInfo", userInfoDTO);
//                    startActivity(intent);
                }
            }
        });
    }
}