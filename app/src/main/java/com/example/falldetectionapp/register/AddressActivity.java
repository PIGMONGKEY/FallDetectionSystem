package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class AddressActivity extends AppCompatActivity {

    private Button toNokPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        init();
    }

    private void init() {
        setView();
        setListener();
    }

    private void setView() {
        toNokPhone = findViewById(R.id.continueToEP);
    }

    private void setListener() {
        toNokPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressActivity.this, NokPhoneActivity.class);
                startActivity(intent);
            }
        });
    }
}