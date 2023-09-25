package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GuideActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

//        setContentView(R.layout.activity_log_in);
//
//        EditText CamId = (EditText) findViewById(R.id.editTextTextPassword);
//        EditText CamPw = (EditText) findViewById(R.id.editTextTextPassword2);
//
//        CheckBox autolog = (CheckBox) findViewById(R.id.checkBox);
//
//        Button lostPw = (Button) findViewById(R.id.button4);
//        Button signupBtn = (Button) findViewById(R.id.button8);
//        Button loginBtn = (Button) findViewById(R.id.button);
//
//        // 자동로그인 부분도 나중에 수정
//        autolog.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
//                if(autolog.isChecked() == true){
//
//                }else{
//
//                }
//
//            }
//        });
//
//        lostPw.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//
//        signupBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), sign_up.class);
//                startActivity(intent);
//            }
//        });
//
//        loginBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text_CamId =CamId.getText().toString().trim();
//                String text_CamPw = CamPw.getText().toString().trim();
//
//                if(!text_CamId.isEmpty() && !text_CamPw.isEmpty())
//                {
//                    //DB 연결해야합니다!!! 만든 후 재수정
//                    //Intent intent = new Intent(getApplicationContext(), home.class);
//                    //startActivity(intent);
//                }
//                else if(text_CamId.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "아이디를 입력해 주세요", Toast.LENGTH_LONG).show();
//                }
//                else if(text_CamPw.isEmpty()){
//                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해 주세요", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
}