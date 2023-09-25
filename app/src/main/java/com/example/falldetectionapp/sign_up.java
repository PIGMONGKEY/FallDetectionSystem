package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class sign_up extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        setTitle("회원가입");
//
//        EditText CamId = (EditText) findViewById(R.id.editTextTextPassword4);
//        EditText CamPw = (EditText) findViewById(R.id.editTextTextPassword5);
//        EditText PwCorect = (EditText) findViewById(R.id.editTextTextPassword6);
//
//        Button btnNext_info = (Button) findViewById(R.id.button3);
//
//        btnNext_info.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String text_CamId = CamId.getText().toString().trim();
//                String text_CamPw = CamPw.getText().toString().trim();
//                String text_PwCorect = PwCorect.getText().toString().trim();
//
//                // 캠 아이디가 DB에 등록 안되어 있는 경우는 안되는걸로
////                if(text_CamId != DB)
////                {
////                    Toast.makeText(getApplicationContext(), "등록되지 않은 아이디입니다.", Toast.LENGTH_LONG).show();
////                }else
////                {
//                if (text_CamPw == text_PwCorect)
//                {
//                    // DB에 등록해야합니다.. 추후 수정!!
//                    Intent intent = new Intent(getApplicationContext(), info.class);
//                    startActivity(intent);
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
//                }
////                }
//            }
//        });
    }
}