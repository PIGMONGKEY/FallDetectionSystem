package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("로딩화면");

        Button btnmain =(Button) findViewById(R.id.button5);

        btnmain.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, address.class);
                startActivity(intent);
            }
        });
    }

//    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!
    //    코드 작성!!!!!!!!!!!!!!!!!!!!!!!!!!!

}