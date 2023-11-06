package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.register.RegisterActivity;

public class StartActivity extends AppCompatActivity {

    private Button toLogin, toRegister, toGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        checkAutoLogin();
        init();
    }

    private void init() {
        setView();
        setListener();
    }

    private void setView() {
        toLogin = findViewById(R.id.toLogin_start);
        toRegister = findViewById(R.id.toRegisterButton_start);
        toGuide = findViewById(R.id.toGuideButton_start);
    }

    private void setListener() {
        toGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, GuideActivity.class);
                startActivity(intent);
            }
        });

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAutoLogin() {
        SharedPreferences sp = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);

        if (sp.contains("auto")) {
            if (sp.getBoolean("auto", false)) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }
    }
}