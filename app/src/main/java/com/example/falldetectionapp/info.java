package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class info extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        setTitle("개인정보");

        EditText Name = (EditText) findViewById(R.id.editTextTextPassword4);
        EditText Phone = (EditText) findViewById(R.id.editTextTextPassword5);
        EditText CfNum = (EditText) findViewById(R.id.editTextTextPassword3);

        Button CfBtn = (Button) findViewById(R.id.button3);
        Button CfOkBtn = (Button) findViewById(R.id.button10);
        Button btnNext = (Button) findViewById(R.id.button9);

        // 인증받기 버튼 수정
        CfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CfBtn.setText("재전송");

            }
        });

        CfOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}