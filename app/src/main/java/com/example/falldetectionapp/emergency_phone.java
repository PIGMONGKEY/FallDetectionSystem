package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class emergency_phone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_phone);
        setTitle("비상연락처 등록");

        EditText emgNum1 = (EditText) findViewById(R.id.editTextTextPostalAddress2);
        Button btnadd = (Button) findViewById(R.id.AddButton);
        Button btnNext_Login = (Button) findViewById(R.id.button7);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnNext_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputNum1 = emgNum1.getText().toString().trim();


                if(!inputNum1.isEmpty())
                {
                    Intent intent = new Intent(getApplicationContext(), log_in.class);
                    startActivity(intent);
                }
            }
        });


    }
}