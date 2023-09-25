package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.R;

/**
 * 회원가입 시, 주소를 입력하는 창입니다.
 * activity_address.xml 과 연결됩니다.
 */
public class AddressActivity extends AppCompatActivity {

    private Button toNokPhone;
    private EditText zipCodeEditText, addressEditText, deepAddressEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        init();
    }

//    초기 설정을 넣으시면 됩니다.
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

//    리스너는 여기에 모아주세요
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