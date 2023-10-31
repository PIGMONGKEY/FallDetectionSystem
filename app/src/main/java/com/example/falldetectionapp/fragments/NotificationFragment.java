package com.example.falldetectionapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.NotificationDTO;
import com.example.falldetectionapp.HomeActivity;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.NotificationService;
import com.example.falldetectionapp.utils.NotificationListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationFragment extends Fragment {

    private ListView notificationListView;
    private String personalToken;
    private String cameraId;

    private HomeActivity homeActivity;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    // 초기 설정
    private void init(View view) {
        setView(view);
        setListener();
        getDataFromBundle();
        requestNotificationBoard();
    }

    // 아이디 매칭
    private void setView(View view) {
        notificationListView = view.findViewById(R.id.notificationListView_notification);
    }

    // 리스너 추가
    private void setListener() {
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                homeActivity.setFrameToNotificationInfo(new NotificationInfoFragment(), position);
            }
        });
    }

    // Bundle로 넘어온 데이터를 받는다.
    private void getDataFromBundle() {
        personalToken = getArguments().getString("personalToken");
        cameraId = getArguments().getString("cameraId");
    }

    // 서버에 공지사항 글 목록을 요청하고 화면에 띄운다.
    private void requestNotificationBoard() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        NotificationService notificationService = retrofit.create(NotificationService.class);

        notificationService.requestAllNotifications().enqueue(new Callback<BasicResponseDTO<ArrayList<NotificationDTO>>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<ArrayList<NotificationDTO>>> call, Response<BasicResponseDTO<ArrayList<NotificationDTO>>> response) {
                if (response.isSuccessful()) {
                    showNotificationList(response.body().getData());
                } else {
                    try {
                        BasicResponseDTO basicResponseDTO = (BasicResponseDTO) retrofit.responseBodyConverter(
                                BasicResponseDTO.class,
                                BasicResponseDTO.class.getAnnotations()
                        ).convert(response.errorBody());

                        showAlertDialog("공지사항", basicResponseDTO.getMessage());

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<ArrayList<NotificationDTO>>> call, Throwable t) {
                showAlertDialog("공지사항", "서버 연결에 실패했습니다.");
            }
        });
    }

    // 화면에 공지사항 리스트를 띄운다.
    private void showNotificationList(ArrayList<NotificationDTO> items) {
        NotificationListAdapter adapter = new NotificationListAdapter(getContext(), items);
        notificationListView.setAdapter(adapter);
    }

    // 확인창 띄우기
    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}