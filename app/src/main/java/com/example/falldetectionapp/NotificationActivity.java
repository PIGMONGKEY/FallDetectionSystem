package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

//        setContentView(R.layout.activity_address);
//        setTitle("주소");
//
//
//        EditText edtPostalAdd = (EditText) findViewById(R.id.editTextTextPostalAddress2);
//        EditText edtAddress = (EditText) findViewById(R.id.editTextTextPostalAddress3);
//        EditText edtDetailAdd = (EditText) findViewById(R.id.editTextTextPostalAddress4);
//
//        Button btnNext_AmgPhone = (Button) findViewById(R.id.button7);
//
//        btnNext_AmgPhone.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String inputText = edtPostalAdd.getText().toString().trim();
//                String inputText2 = edtAddress.getText().toString().trim();
//                String inputText3 = edtDetailAdd.getText().toString().trim();
//
//                if(!inputText.isEmpty() && !inputText2.isEmpty()) {
//                    Intent intent = new Intent(getApplicationContext(), emergency_phone.class);
//                    startActivity(intent);
//                }
//                else{
//                    Toast.makeText(getApplicationContext(), "주소를 모두 입력해 주세요", Toast.LENGTH_LONG).show();
//                }
//            }
//        });
    }
}