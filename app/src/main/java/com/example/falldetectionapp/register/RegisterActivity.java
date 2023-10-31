package com.example.falldetectionapp.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.LoginActivity;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 회원가입 시, 카메라ID와 비밀번호를 입력하는 창입니다.
 * activity_sign_up.xml과 연결됩니다.
 */
public class RegisterActivity extends AppCompatActivity {

    private Button toInfoButton;
    private EditText cameraIdEditText, passwordEditText, passwordCheckEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_sign_up);

        init();
    }

//    초기 설정을 넣어주세요
    private void init() {
        setTitle("회원가입");
        setView();
        setListener();
    }

    private void setView() {
        toInfoButton = findViewById(R.id.continueToInfoButton);
        cameraIdEditText = findViewById(R.id.cameraIdEditText_register);
        passwordEditText = findViewById(R.id.passwordEditText_register);
        passwordCheckEditText = findViewById(R.id.passwordCheckEditText_register);
    }

//    리스너는 여기 모아주세요
    private void setListener() {
        toInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cameraId = cameraIdEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                String passwordCheck = passwordCheckEditText.getText().toString().trim();

                if (inputCheck(cameraId, password, passwordCheck)) {
                    // 등록 가능한 카메라 아이디 인지 서버에 확인을 요청한다.
                    checkCameraId(cameraId);
                }
            }
        });
    }

    // 빈 칸이 없고, 비밀번호와 비밀번호 확인이 서로 일치하면 True를 반환한다.
    // 그렇지 않으면 False를 반환한다.
    private boolean inputCheck(String cameraId, String password, String passwordCheck) {
        if (cameraId.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
            Toast.makeText(this, "모든 항목을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            if (password.equals(passwordCheck)) {
                return true;
            } else {
                Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
    }

    /**
     * 서버로 요청을 보내서 존재하는 카메라 ID인지 확인한다.
     * @param cameraId
     */
    private void checkCameraId(String cameraId) {

        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
        .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
        .build();

        UserService userService = retrofit.create(UserService.class);

        userService.checkCameraId(cameraId).enqueue(new Callback<BasicResponseDTO>() {
            @Override
            public void onResponse(Call<BasicResponseDTO> call, Response<BasicResponseDTO> response) {
                if (response.isSuccessful()) {
                    // 등록 가능한 카메라 아이디
                    if (passwordEditText.getText().toString().equals(passwordCheckEditText.getText().toString())) {

                        // 사용자 정보 DTO 객체 생성 및 데이터 삽입
                        UserInfoDTO userInfoDTO = new UserInfoDTO();
                        userInfoDTO.setCameraId(cameraId);
                        userInfoDTO.setUserPassword(passwordEditText.getText().toString());

                        // Intent에 삽입하여 전달
                        Intent intent = new Intent(RegisterActivity.this, InfoActivity.class);
                        intent.putExtra("userInfo", userInfoDTO);
                        startActivity(intent);

                    } else {
                        showAlertDialog("회원가입", "비밀번호가 서로 일치하지 않습니다.");
                    }
                } else {
                    // 등록 불가능한 아이디
                    // 300 이상의 코드는 response.isSuccessful()을 false로 반환함
                    // 따라서 errorBody에 담겨오기 때문에 errorBody를 BasicResponseDTO에 매핑하여 값을 얻음
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());

                        showAlertDialog("회원가입", basicResponseDTO.getMessage());

//                        if (basicResponseDTO.getCode() == 404) {
//                            // 없는 카메라 아이디
//                            Toast.makeText(getApplicationContext(), basicResponseDTO.getMessage(), Toast.LENGTH_SHORT).show();
//                        } else {
//                            // 이미 등록된 카메라 아이디
//                            Toast.makeText(getApplicationContext(), basicResponseDTO.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO> call, Throwable t) {
                // 서버 연결에 실패한 경우 onFailure로 들어옴
                Toast.makeText(getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}