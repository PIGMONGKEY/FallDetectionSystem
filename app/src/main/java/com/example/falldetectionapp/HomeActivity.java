package com.example.falldetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.falldetectionapp.fragments.GuideFragment;
import com.example.falldetectionapp.fragments.HomeFragment;
import com.example.falldetectionapp.fragments.MyPageFragment;
import com.example.falldetectionapp.fragments.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {

    public BottomNavigationView bottomNavigationView;
    private String personalToken;
    private String cameraId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();
    }

    // 화면이 생성될 때 실행시켜줌으로써, 초기 설정을 한다.
    private void init() {
        getDataFromIntent();
        setViews();
        setBottomNavigationView();
    }

    // Intent로 넘어온 데이터 받기
    private void getDataFromIntent() {
        Intent intent = getIntent();
        personalToken = intent.getStringExtra("personalToken");
        cameraId = intent.getStringExtra("cameraId");
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
    public void setFrame(int id) {
        Fragment newFragment;
        Bundle bundle = new Bundle();

        if (id == R.id.home) {
            newFragment = new HomeFragment();
            bundle.putString("cameraId", cameraId);
            bundle.putString("personalToken", personalToken);
            newFragment.setArguments(bundle);
        } else if (id == R.id.video) {
            newFragment = new VideoFragment();
            bundle.putString("cameraId", cameraId);
            newFragment.setArguments(bundle);
        } else if (id == R.id.myPage) {
            newFragment = new MyPageFragment();
            bundle.putString("cameraId", cameraId);
            bundle.putString("personalToken", personalToken);
            newFragment.setArguments(bundle);
        } else {
            newFragment = new GuideFragment();
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, newFragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }
}