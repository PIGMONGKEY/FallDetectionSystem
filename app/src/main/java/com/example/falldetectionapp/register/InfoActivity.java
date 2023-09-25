package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.text.IDNA;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.falldetectionapp.R;

public class InfoActivity extends AppCompatActivity {

    private Button toAddressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        init();
    }

    private void init() {
        setView();
        setListener();
    }

    private void setView() {
        toAddressButton = findViewById(R.id.continueToAddressButton);
    }

    private void setListener() {
        toAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InfoActivity.this, AddressActivity.class);
                startActivity(intent);
            }
        });
    }
}