package com.example.falldetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.fragments.HomeFragment;
import com.example.falldetectionapp.fragments.MyPageFragment;
import com.example.falldetectionapp.fragments.VideoFragment;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private String personalToken;
    private UserInfoDTO userInfoDTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }


    //     화면이 생성될 때 실행시켜줌으로써, 초기 설정을 한다.
    private void init() {
        getDataFromIntent();
        setViews();
        setBottomNavigationView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        personalToken = intent.getStringExtra("personalToken");
        requestUserInfo(intent.getStringExtra("cameraId"));
    }

    private void requestUserInfo(String cameraId) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:10000") // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        userService.getUserInfo("Bearer " + personalToken, cameraId).enqueue(new Callback<UserInfoDTO>() {
            @Override
            public void onResponse(Call<UserInfoDTO> call, Response<UserInfoDTO> response) {
                if (response.body().getRequestSuccess().trim().equals("Success")) {
                    userInfoDTO = new UserInfoDTO();
                    userInfoDTO.setUserName(response.body().getUserName());
                    userInfoDTO.setCameraId(response.body().getCameraId());
                    userInfoDTO.setUserPhone(response.body().getUserPhone());
                    userInfoDTO.setUserAddress(response.body().getUserAddress());
                    userInfoDTO.setNokPhones(response.body().getNokPhones());
                } else {
                    Toast.makeText(getApplicationContext(), "사용자 정보를 가져오는데 실패했습니다.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<UserInfoDTO> call, Throwable t) {
                Log.d("HOME", t.getMessage());
            }
        });
    }

    //    findViewById 메소드를 이용해서 멤버 변수와 View를 연결한다.
    private void setViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

    //     하단 네비게이션 바 아이템 클릭 리스너 설정
    private void setBottomNavigationView() {
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFrame(item.getItemId());
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.home);
    }

    /**
     * 클릭된 메뉴 항목의 아이디를 받아와서, FrameLayout의 출력 fragment를 변경한다.
     * @param id 선택된 항목의 아이디를 파라미터로 받는다. ex) R.id.home
     */
    private void setFrame(int id) {
        Fragment newFragment;
        if (id == R.id.home) {
            newFragment = new HomeFragment();
        } else if (id == R.id.video) {
            newFragment = new VideoFragment();
        } else {
            newFragment = new MyPageFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}