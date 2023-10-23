package com.example.falldetectionapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.HomeActivity;
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
 * HomeActivity의 FrameLayout에 들어가는 HomeFragment입니다.
 * 화면 안에 들어가는 화면이라고 생각하시면 됩니다.
 * 하단 네비게이션 바를 클릭하면 다른 화면으로 바뀝니다.
 * 위 로직은 HomeActivity에서 구현되어있습니다.
 *
 * Java 코드를 작성하실 때, Fragment에 대해 조금 찾아보시면서 작성하셔야 할 듯 합니다.
 * Activity와는 다르게 독자적으로 존재할 수 없는 화면이고, 화면 안에 있는 화면이다보니, 로직을 짜는 방식이 조금 다를껍니다.
 */
// TODO: 홈 화면 지금대로 한다면 주석 처리한 부분 삭제
// TODO: 공지사항 페이지 제작
public class HomeFragment extends Fragment {

    private HomeActivity homeActivity;

    private Button toGuideButton, toVideoButton, toMyPageButton, toNotiBoardButton;
//    private TextView nameTV, genderTV, ageTV, bloodTypeTV;
    private String cameraId, personalToken;
    private UserInfoDTO userInfoDTO;

//    빈 생성자가 있어야 합니다. 삭제하면 안됩니다.
    public HomeFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        getDataFromBundle();
        setView(view);
        setListener();
//        requestUserInfo();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        homeActivity = (HomeActivity) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        homeActivity = null;
    }

    private void getDataFromBundle() {
        cameraId = getArguments().getString("cameraId");
        personalToken = getArguments().getString("personalToken");
    }

    private void setView(View view) {
        toGuideButton = view.findViewById(R.id.toGuideButton);
        toVideoButton = view.findViewById(R.id.toVideoButton);
        toMyPageButton = view.findViewById(R.id.toMyPageButton);
        toNotiBoardButton = view.findViewById(R.id.toNotificationBoardButton);
//        nameTV = view.findViewById(R.id.userName_home);
//        ageTV = view.findViewById(R.id.userAge_home);
//        genderTV = view.findViewById(R.id.userGender_home);
//        bloodTypeTV = view.findViewById(R.id.userBloodType_home);
    }

    private void setListener() {
        toGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.setFrame(R.id.guide);
                homeActivity.bottomNavigationView.setSelectedItemId(R.id.guide);
            }
        });

        toVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.setFrame(R.id.video);
                homeActivity.bottomNavigationView.setSelectedItemId(R.id.video);
            }
        });

        toMyPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeActivity.setFrame(R.id.myPage);
                homeActivity.bottomNavigationView.setSelectedItemId(R.id.myPage);
            }
        });

        toNotiBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void showUserInfo() {
//        nameTV.setText(userInfoDTO.getUserName());
//        genderTV.setText(userInfoDTO.getUserGender());
//        ageTV.setText(userInfoDTO.getUserAge().toString());
//        bloodTypeTV.setText(userInfoDTO.getUserBloodType());
    }

    private void requestUserInfo() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        userService.getUserInfo("Bearer " + personalToken, cameraId).enqueue(new Callback<BasicResponseDTO<UserInfoDTO>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<UserInfoDTO>> call, Response<BasicResponseDTO<UserInfoDTO>> response) {
                if (response.isSuccessful()) {
                    // 유저 정보 조회 성공
                    userInfoDTO = response.body().getData();
                    // 유저 정보 화면에 띄우기
                    showUserInfo();
                } else {
                    // 유저 정보 조회 실패
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());

                        // 오류 메시지 띄우고, 로그인 창으로 돌려보냄
                        Toast.makeText(getContext(), basicResponseDTO.getMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getContext(), LoginActivity.class);
                        startActivity(intent);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<UserInfoDTO>> call, Throwable t) {
                Log.d("HOME", t.getMessage());
                Toast.makeText(getContext().getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
}