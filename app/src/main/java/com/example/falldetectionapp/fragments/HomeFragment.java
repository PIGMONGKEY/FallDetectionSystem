package com.example.falldetectionapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.falldetectionapp.GuideActivity;
import com.example.falldetectionapp.R;

/**
 * HomeActivity의 FrameLayout에 들어가는 HomeFragment입니다.
 * 화면 안에 들어가는 화면이라고 생각하시면 됩니다.
 * 하단 네비게이션 바를 클릭하면 다른 화면으로 바뀝니다.
 * 위 로직은 HomeActivity에서 구현되어있습니다.
 *
 * Java 코드를 작성하실 때, Fragment에 대해 조금 찾아보시면서 작성하셔야 할 듯 합니다.
 * Activity와는 다르게 독자적으로 존재할 수 없는 화면이고, 화면 안에 있는 화면이다보니, 로직을 짜는 방식이 조금 다를껍니다.
 */
public class HomeFragment extends Fragment {

    private Button toGuideButton, toVideoButton, toMyPageButton;

//    빈 생성자가 있어야 합니다. 삭제하면 안됩니다.
    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setView(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    private void init() {
        setListener();
    }

    private void setView(View view) {
        toGuideButton = view.findViewById(R.id.toGuideButton);
        toVideoButton = view.findViewById(R.id.toVideoButton);
        toMyPageButton = view.findViewById(R.id.toMyPageButton);
    }

    private void setListener() {
        toGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GuideActivity.class);
                startActivity(intent);
            }
        });
    }
}