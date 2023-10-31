package com.example.falldetectionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.NotificationDTO;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.NotificationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class NotificationInfoFragment extends Fragment {

    private TextView titleTV, contentTV, regdateTV, updatedateTV;
    private String cameraId, personalToken;
    private int bno;

    public NotificationInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification_info, container, false);

        init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        setView(view);
        getDateFromBundle();
        requestNotificationInfo();
    }

    private void setView(View view) {
        titleTV = view.findViewById(R.id.titleTextView_notification_info);
        contentTV = view.findViewById(R.id.contentTextView_notification_info);
        regdateTV = view.findViewById(R.id.regdateTextView_notification_info);
        updatedateTV = view.findViewById(R.id.updatedateTextView_notification_info);
    }

    private void getDateFromBundle() {
        cameraId = getArguments().getString("cameraId");
        personalToken = getArguments().getString("personalToken");
        bno = getArguments().getInt("bno");
    }

    private void requestNotificationInfo() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        NotificationService notificationService = retrofit.create(NotificationService.class);

        notificationService.requestOneNotification(bno + 1).enqueue(new Callback<BasicResponseDTO<NotificationDTO>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<NotificationDTO>> call, Response<BasicResponseDTO<NotificationDTO>> response) {
                if (response.isSuccessful()) {
                    showNotificationContent(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<NotificationDTO>> call, Throwable t) {

            }
        });
    }

    private void showNotificationContent(NotificationDTO notificationDTO) {
        titleTV.setText(notificationDTO.getTitle());
        contentTV.setText(notificationDTO.getNotiContent());
        regdateTV.setText("등록 " + notificationDTO.getRegdate());
        if (notificationDTO.getRegdate().equals(notificationDTO.getUpdatedate())) {
            updatedateTV.setVisibility(View.GONE);
        } else {
            updatedateTV.setText("수정 " + notificationDTO.getUpdatedate());
        }
    }
}