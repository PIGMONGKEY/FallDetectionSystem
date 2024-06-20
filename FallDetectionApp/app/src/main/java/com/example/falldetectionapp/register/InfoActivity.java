package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.R;

/**
 * 회원가입 시, 사용자 정보를 입력하는 창입니다.
 * activity_info.xml과 연결됩니다.
 */
public class InfoActivity extends AppCompatActivity {
    private Button toAddressButton;
    private EditText nameEditText, phoneEditText, ageEditText;
    private RadioGroup genderRadioGroup, bloodRadioGroup;
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
        genderRadioGroup = findViewById(R.id.genderRadioGroup_register);
        bloodRadioGroup = findViewById(R.id.bloodTypeRadioGroup_register);
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

                if (inputCheck(name, phone, age)) {
                    userInfoDTO.setUserPhone(phone);
                    userInfoDTO.setUserName(name);
                    userInfoDTO.setUserAge(Integer.parseInt(age));

                    Intent intent = new Intent(InfoActivity.this, AddressActivity.class);
                    intent.putExtra("userInfo", userInfoDTO);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        genderRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.MALE) {
                    userInfoDTO.setUserGender("남성");
                } else if (checkedId == R.id.FEMALE) {
                    userInfoDTO.setUserGender("여성");
                }
            }
        });

        bloodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.A) {
                    userInfoDTO.setUserBloodType("A형");
                } else if (checkedId == R.id.B) {
                    userInfoDTO.setUserBloodType("B형");
                } else if (checkedId == R.id.O) {
                    userInfoDTO.setUserBloodType("O형");
                } else if (checkedId == R.id.AB) {
                    userInfoDTO.setUserBloodType("AB형");
                }
            }
        });
    }

    private boolean inputCheck(String name, String phone, String age) {
        if ((name.isEmpty() || phone.isEmpty() || age.isEmpty())
                && !genderRadioGroup.isSelected()
                && !bloodRadioGroup.isSelected()) {
            return false;
        } else {
            return true;
        }
    }
}