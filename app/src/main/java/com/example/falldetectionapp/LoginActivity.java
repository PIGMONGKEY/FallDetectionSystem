package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.register.RegisterActivity;

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

    private void setListener() {
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cameraId = cameraIdEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (!cameraId.isEmpty() && !password.isEmpty()) {
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    startActivity(intent);
                } else if (cameraId.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_LONG).show();
                }
            }
        });

        autoLoginCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                } else {

                }
            }
        });

        forgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}