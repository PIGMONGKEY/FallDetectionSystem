package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.R;

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
                String password = passwordEditText.getText().toString().trim();
                String passwordCheck = passwordCheckEditText.getText().toString().trim();

//                캠아이디가 DB에 등록되어 있지 않은 경우는 안되는 걸로
//                if (cameraId != DB) {
//                    Toast.makeText(getApplicationContext(), "등록되지 않은 카메라 아이디 입니다.", Toast.LENGTH_LONG).show();
//                }

                if (password == passwordCheck) {
                    Intent intent = new Intent(RegisterActivity.this, InfoActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}