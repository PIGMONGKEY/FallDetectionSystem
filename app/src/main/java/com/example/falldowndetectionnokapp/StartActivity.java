package com.example.falldowndetectionnokapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                showAlertDialog("입력하신 전화번호는 " + phone + " 입니다.\n" +
                        "이후 입력하신 번호 변경을 위해서는 앱을 다시 설치하여야 합니다.\n" +
                        "올바르게 입력하셨다면, \"확인\"을 눌러주세요.");
            }
        });
    }

    private void showAlertDialog(String message) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO: 저장 HTTP 요청
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();
    }
}