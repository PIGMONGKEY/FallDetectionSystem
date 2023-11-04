package com.example.falldetectionapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.AuthTokenDTO;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.UserInfoDTO;
import com.example.falldetectionapp.HomeActivity;
import com.example.falldetectionapp.LoginActivity;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.StartActivity;
import com.example.falldetectionapp.retrofit.AuthService;
import com.example.falldetectionapp.retrofit.UserService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
public class MyPageFragment extends Fragment {
    private ImageView profileImageView;
    private TextView nameTV, genderTV, ageTV, bloodTypeTV, cameraIdTV, phoneTV, addressTV, nokPhone1TV, nokPhone2TV;
    private EditText phoneET, addressET, nokPhone1ET, nokPhone2ET;
    private Button modifyUserInfoBTN, logoutBTN, signoutBTN, modifyConfirmInfoBTN;
    private LinearLayout nokPhone_2_LinearLayout;
    private ImageButton addNokPhoneBTN, dropNokPhoneBTN;
    private View lineUnderNokphone2;
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
        setListeners();
        getDataFromBundle();
    }

    private void setViews(View view) {
        profileImageView = view.findViewById(R.id.profileImageView_myPage);
        nameTV = view.findViewById(R.id.nameTextView_myPage);
        ageTV = view.findViewById(R.id.ageTextView_myPage);
        genderTV = view.findViewById(R.id.sexTextView_myPage);
        bloodTypeTV = view.findViewById(R.id.bloodTypeTextView_myPage);
        cameraIdTV = view.findViewById(R.id.cameraIDTextView_myPage);
        phoneTV = view.findViewById(R.id.phoneTextView_myPage);
        addressTV = view.findViewById(R.id.addressTextView_myPage);
        nokPhone1TV = view.findViewById(R.id.nokPhone_1_TextView_myPage);
        nokPhone2TV = view.findViewById(R.id.nokPhone_2_TextView_myPage);
        modifyUserInfoBTN = view.findViewById(R.id.modifyInfoButton_mypage);
        logoutBTN = view.findViewById(R.id.logoutButton_mypage);
        signoutBTN = view.findViewById(R.id.signOutButton_mypage);
        nokPhone_2_LinearLayout = view.findViewById(R.id.nokPhone_2_LinearLayout_mypage);

        phoneET = view.findViewById(R.id.phoneEditTExt_myPage);
        addressET = view.findViewById(R.id.addressEditText_myPage);
        nokPhone1ET = view.findViewById(R.id.nokPhone_1_EditText_myPage);
        nokPhone2ET = view.findViewById(R.id.nokPhone_2_EditText_myPage);

        modifyConfirmInfoBTN = view.findViewById(R.id.confirmModifyInfoButton_mypage);

        addNokPhoneBTN = view.findViewById(R.id.add_nokphone_button_mypage);
        dropNokPhoneBTN = view.findViewById(R.id.dropNokPhone2Button_myPage);
        lineUnderNokphone2 = view.findViewById(R.id.lineUnderNokPhone2);
    }

    private void setListeners() {
        // 개인정보 수정 버튼 클릭
        modifyUserInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addressTV.setVisibility(View.GONE);
                addressET.setVisibility(View.VISIBLE);

                phoneTV.setVisibility(View.GONE);
                phoneTV.setVisibility(View.VISIBLE);

                nokPhone1TV.setVisibility(View.GONE);
                nokPhone1ET.setVisibility(View.VISIBLE);

                if (nokPhone_2_LinearLayout.getVisibility() != View.VISIBLE) {
                    lineUnderNokphone2.setVisibility(View.GONE);
                    addNokPhoneBTN.setVisibility(View.VISIBLE);
                }

                nokPhone2ET.setVisibility(View.VISIBLE);
                nokPhone2TV.setVisibility(View.GONE);
                dropNokPhoneBTN.setVisibility(View.VISIBLE);

                modifyConfirmInfoBTN.setVisibility(View.VISIBLE);
                modifyUserInfoBTN.setVisibility(View.GONE);
            }
        });

        // 수정 완료 버튼 클릭
        modifyConfirmInfoBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoDTO.setUserPhone(phoneET.getText().toString());
                userInfoDTO.setUserAddress(addressET.getText().toString());

                List<String> tempNokPhones = new ArrayList<>();
                tempNokPhones.add(nokPhone1ET.getText().toString().trim());
                if (!nokPhone2ET.getText().toString().equals("")) {
                    tempNokPhones.add(nokPhone2ET.getText().toString().trim());
                }

                userInfoDTO.setNokPhones(tempNokPhones);

                requestModifyUserInfo();
            }
        });

        // 로그아웃 버튼 클릭
        logoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestLogout();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        // 탈퇴 버튼 클릭
        signoutBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("서비스 탈퇴")
                        .setMessage("정말 탈퇴 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestSignOut();
                            }
                        })
                        .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

        // 보호자 번호 추가 버튼
        addNokPhoneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nokPhone_2_LinearLayout.setVisibility(View.VISIBLE);
                lineUnderNokphone2.setVisibility(View.VISIBLE);
                addNokPhoneBTN.setVisibility(View.GONE);
            }
        });

        // 보호자 번호 삭제 버튼
        dropNokPhoneBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nokPhone_2_LinearLayout.setVisibility(View.GONE);
                nokPhone2ET.setText("");
                lineUnderNokphone2.setVisibility(View.GONE);
                addNokPhoneBTN.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getDataFromBundle() {
        personalToken = getArguments().getString("personalToken");
        requestUserInfo(getArguments().getString("cameraId"));
    }

    // 서버에 cameraId를 넘겨서 사용자 정보 받아오기
    private void requestUserInfo(String cameraId) {
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
                        Intent intent = new Intent(getActivity(), StartActivity.class);
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

    // 로그아웃 요청
    private void requestLogout() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        AuthService authService = retrofit.create(AuthService.class);

        authService.requestLogout("Bearer " + personalToken, new AuthTokenDTO(personalToken)).enqueue(new Callback<BasicResponseDTO<AuthTokenDTO>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<AuthTokenDTO>> call, Response<BasicResponseDTO<AuthTokenDTO>> response) {
                new AlertDialog.Builder(getContext())
                        .setTitle("로그아웃")
                        .setMessage("로그아웃 되었습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getActivity(), StartActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<AuthTokenDTO>> call, Throwable t) {

            }
        });
    }

    // 탈퇴 요청
    private void requestSignOut() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        userService.signOut("Bearer " + personalToken, userInfoDTO.getCameraId()).enqueue(new Callback<BasicResponseDTO>() {
            @Override
            public void onResponse(Call<BasicResponseDTO> call, Response<BasicResponseDTO> response) {
                if (response.isSuccessful()) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("탈퇴")
                            .setMessage("탈퇴처리 되었습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getActivity(), StartActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                } else {
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());

                        // 오류 메시지 띄우고, 로그인 창으로 돌려보냄
                        new AlertDialog.Builder(getContext())
                                .setTitle("탈퇴")
                                .setMessage("오류가 발생했습니다.")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(getActivity(), StartActivity.class);
                                        startActivity(intent);
                                    }
                                }).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO> call, Throwable t) {

            }
        });
    }

    private void requestModifyUserInfo() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        UserService userService = retrofit.create(UserService.class);

        userService.updateUserInfo("Bearer " + personalToken, userInfoDTO).enqueue(new Callback<BasicResponseDTO>() {
            @Override
            public void onResponse(Call<BasicResponseDTO> call, Response<BasicResponseDTO> response) {
                if (response.isSuccessful()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("개인정보 수정")
                            .setMessage("수정 완료되었습니다.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showUserInfo();
                                }
                            }).show();
                } else {
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());

                        // 오류 메시지 띄우고, 로그인 창으로 돌려보냄
                        new AlertDialog.Builder(getActivity())
                                .setTitle("개인정보 수정")
                                .setMessage(basicResponseDTO.getMessage())
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO> call, Throwable t) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("개인정보 수정")
                        .setMessage("서버 연결에 실패했습니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });
    }

    // 사용자 정보 표시
    private void showUserInfo() {
        phoneET.setVisibility(View.GONE);
        phoneTV.setVisibility(View.VISIBLE);
        addressET.setVisibility(View.GONE);
        addressTV.setVisibility(View.VISIBLE);
        nokPhone1ET.setVisibility(View.GONE);
        nokPhone1TV.setVisibility(View.VISIBLE);
        nokPhone2ET.setVisibility(View.GONE);
        nokPhone2TV.setVisibility(View.VISIBLE);
        dropNokPhoneBTN.setVisibility(View.GONE);
        addNokPhoneBTN.setVisibility(View.GONE);

        modifyConfirmInfoBTN.setVisibility(View.GONE);
        modifyUserInfoBTN.setVisibility(View.VISIBLE);

        nameTV.setText(userInfoDTO.getUserName());
        ageTV.setText(userInfoDTO.getUserAge().toString() + "세");
        genderTV.setText(userInfoDTO.getUserGender());
        bloodTypeTV.setText(userInfoDTO.getUserBloodType());
        cameraIdTV.setText(userInfoDTO.getCameraId());
        phoneTV.setText(userInfoDTO.getUserPhone());
        phoneET.setText(userInfoDTO.getUserPhone());
        addressTV.setText(userInfoDTO.getUserAddress());
        addressET.setText(userInfoDTO.getUserAddress());
        nokPhone1TV.setText(userInfoDTO.getNokPhones().get(0));
        nokPhone1ET.setText(userInfoDTO.getNokPhones().get(0));
        if (userInfoDTO.getNokPhones().size() > 1) {
            nokPhone2TV.setText(userInfoDTO.getNokPhones().get(1));
            nokPhone2ET.setText(userInfoDTO.getNokPhones().get(1));
        } else {
            nokPhone_2_LinearLayout.setVisibility(View.GONE);
            lineUnderNokphone2.setVisibility(View.GONE);
        }
        if (userInfoDTO.getUserGender().equals("남성")) {
            profileImageView.setImageResource(R.drawable.profile_man);
        } else {
            profileImageView.setImageResource(R.drawable.profile_woman);
        }
    }
}
