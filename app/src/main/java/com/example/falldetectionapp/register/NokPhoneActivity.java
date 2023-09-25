package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.GuideActivity;
import com.example.falldetectionapp.HomeActivity;
import com.example.falldetectionapp.R;

public class NokPhoneActivity extends AppCompatActivity {

    private Button registerDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_phone);

        init();
    }

    private void init() {
        setView();
        setListener();
    }

    private void setView() {
        registerDone = findViewById(R.id.finishButton_register);
    }

    private void setListener() {
        registerDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NokPhoneActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });
    }
}