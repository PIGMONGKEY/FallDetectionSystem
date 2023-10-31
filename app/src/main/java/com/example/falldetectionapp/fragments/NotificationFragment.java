package com.example.falldetectionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.DTO.NotificationDTO;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.NotificationService;
import com.example.falldetectionapp.utils.NotificationListAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    private void init(View view) {
        setView(view);
        setListener();
        getDataFromBundle();
        requestNotificationBoard();
    }

    private void setView(View view) {
        notificationListView = view.findViewById(R.id.notificationListView_notification);
    }

    private void setListener() {
        notificationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    private void getDataFromBundle() {
        personalToken = getArguments().getString("personalToken");
        cameraId = getArguments().getString("cameraId");
    }

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

                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<ArrayList<NotificationDTO>>> call, Throwable t) {

            }
        });
    }

    private void showNotificationList(ArrayList<NotificationDTO> items) {
        NotificationListAdapter adapter = new NotificationListAdapter(getContext(), items);
        notificationListView.setAdapter(adapter);
    }

    private void showAlertDialog(String title, String message) {

    }
}