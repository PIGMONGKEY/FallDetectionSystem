package com.example.falldetectionapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        setViews();
        setBottomNavigationView();
    }

    private void setViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
    }

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

    private void setFrame(int id) {
        String tag = String.valueOf(id);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment currentFragment = fragmentManager.getPrimaryNavigationFragment();

        if (currentFragment != null) {
            fragmentTransaction.hide(currentFragment);
        }

        Fragment newFragment = fragmentManager.findFragmentByTag(tag);
        if (newFragment == null) {
            if (id == R.id.home) {

            } else if (id == R.id.video) {

            } else if (id == R.id.myPage) {

            } else {

            }

            fragmentTransaction.add(R.id.frameLayout, newFragment, tag);
        } else {
            fragmentTransaction.show(newFragment);
        }
    }
}