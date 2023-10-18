package com.example.falldetectionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * HomeActivity의 FrameLayout에 들어가는 MyPageFragment입니다.
 * 화면 안에 들어가는 화면이라고 생각하시면 됩니다.
 * 하단 네비게이션 바를 클릭하면 다른 화면으로 바뀝니다.
 * 위 로직은 HomeActivity에서 구현되어있습니다.
 *
 * Java 코드를 작성하실 때, Fragment에 대해 조금 찾아보시면서 작성하셔야 할 듯 합니다.
 * Activity와는 다르게 독자적으로 존재할 수 없는 화면이고, 화면 안에 있는 화면이다보니, 로직을 짜는 방식이 조금 다를껍니다.
 */
// TODO: 로그아웃, 탈퇴, 회원정보 수정 구현
public class MyPageFragment extends Fragment {
    private TextView nameTV, phoneTV, addressTV, nokPhone1TV, nokPhone2TV;
    private String personalToken;
    private UserInfoDTO userInfoDTO;

//    빈 생성자가 있어야 합니다. 삭제하면 안됩니다.
    public MyPageFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_page, container, false);
        init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        setViews(view);
        getDataFromBundle();
    }

    private void setViews(View view) {
        nameTV = view.findViewById(R.id.nameTextView_myPage);
        phoneTV = view.findViewById(R.id.phoneTextView_myPage);
        addressTV = view.findViewById(R.id.addressTextView_myPage);
        nokPhone1TV = view.findViewById(R.id.nokPhone_1_TextView_myPage);
        nokPhone2TV = view.findViewById(R.id.nokPhone_2_TextView_myPage);
    }

    private void getDataFromBundle() {
        personalToken = getArguments().getString("personalToken");
//        requestUserInfo(getArguments().getString("cameraId"));

        nameTV.setText("userInfoDTO.getUserName()");
        phoneTV.setText("userInfoDTO.getUserPhone()");
        addressTV.setText("userInfoDTO.getUserAddress()");
        nokPhone1TV.setText("userInfoDTO.getNokPhones().get(0)");
        nokPhone2TV.setText("userInfoDTO.getNokPhones().get(1)");
    }

    // 서버에 cameraId를 넘겨서 사용자 정보 받아오기
    private void requestUserInfo(String cameraId) {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:10000/") // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        userService.getUserInfo("Bearer " + personalToken, cameraId).enqueue(new Callback<BasicResponseDTO<UserInfoDTO>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<UserInfoDTO>> call, Response<BasicResponseDTO<UserInfoDTO>> response) {
                if (response.body().getCode() == 200) {
                    userInfoDTO = response.body().getData();
                    showInfo();
                } else if (response.body().getCode() == 405){
                    Toast.makeText(getContext().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext().getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<UserInfoDTO>> call, Throwable t) {
                Log.d("HOME", t.getMessage());
                Toast.makeText(getContext().getApplicationContext(), "서버 연결에 실패했습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showInfo() {
        nameTV.setText(userInfoDTO.getUserName());
        phoneTV.setText(userInfoDTO.getUserPhone());
        addressTV.setText(userInfoDTO.getUserAddress());
        nokPhone1TV.setText(userInfoDTO.getNokPhones().get(0));
        if (userInfoDTO.getNokPhones().size() > 1) {
            nokPhone2TV.setText(userInfoDTO.getNokPhones().get(1));
        }
    }
}
