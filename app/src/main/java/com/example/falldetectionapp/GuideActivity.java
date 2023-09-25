package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

/**
 * 가이드를 보여주는 창입니다.
 * activity_guide.xml과 연결됩니다.
 */
public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        init();
    }

//    초기 설정을 넣어주세요
    private void init() {
        setView();
        setListener();
    }

    private void setView() {

    }

//    리스너는 여기 모아주세요
    private void setListener() {

    }
}