package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.R;

/**
 * 회원가입 시, 사용자 정보를 입력하는 창입니다.
 * activity_info.xml과 연결됩니다.
 */
// TODO: 성별 radio로 변경
// TODO: 혈액형 입력 방식 변경
public class InfoActivity extends AppCompatActivity {
    private Button toAddressButton;
    private EditText nameEditText, phoneEditText, ageEditText, genderEditText, bloodTypeEditText;
    private UserInfoDTO userInfoDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);

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
        nameEditText = findViewById(R.id.nameEditText_register);
        phoneEditText = findViewById(R.id.phoneEditText_register);
        ageEditText = findViewById(R.id.ageEditText_register);
        genderEditText = findViewById(R.id.genderEditText_register);
        bloodTypeEditText = findViewById(R.id.bloodTypeEditText_register);
    }

//    리스너는 여기에 모아주세요
    private void setListener() {
        // 다음 버튼
        toAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String age = ageEditText.getText().toString().trim();
                String gender = genderEditText.getText().toString().trim();
                String bloodType = bloodTypeEditText.getText().toString().trim();

                if (inputCheck(name, phone, age, gender, bloodType)) {
                    userInfoDTO.setUserPhone(phone);
                    userInfoDTO.setUserName(name);
                    userInfoDTO.setUserAge(Integer.parseInt(age));
                    userInfoDTO.setUserGender(gender);
                    userInfoDTO.setUserBloodType(bloodType);

                    Intent intent = new Intent(InfoActivity.this, AddressActivity.class);
                    intent.putExtra("userInfo", userInfoDTO);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean inputCheck(String name, String phone, String age, String gender, String bloodType) {
        if (name.isEmpty() || phone.isEmpty() || age.isEmpty() || gender.isEmpty() || bloodType.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}