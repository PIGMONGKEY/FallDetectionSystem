package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.R;

public class AddressActivity extends AppCompatActivity {

    private Button toNokPhone;
    private EditText zipCodeEditText, addressEditText, deepAddressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        init();
    }

    private void init() {
        setTitle("주소");
        setView();
        setListener();
    }

    private void setView() {
        toNokPhone = findViewById(R.id.continueToEP);
        zipCodeEditText = findViewById(R.id.zipCodeEditText_register);
        addressEditText = findViewById(R.id.addressEditText_register);
        deepAddressEditText = findViewById(R.id.addressDeepEditText_register);
    }

    private void setListener() {
        toNokPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCode = zipCodeEditText.getText().toString().trim();
                String address = addressEditText.getText().toString().trim();
                String addressDeep = deepAddressEditText.getText().toString().trim();

                if (!zipCode.isEmpty() && !address.isEmpty() && !addressDeep.isEmpty()) {
                    Intent intent = new Intent(AddressActivity.this, NokPhoneActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "주소를 모두 입력해 주세요", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}