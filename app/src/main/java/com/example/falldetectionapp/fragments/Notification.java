package com.example.falldetectionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.falldetectionapp.R;

public class Notification extends Fragment {

    private ListView notificationListView;
    private String personalToken;
    private String cameraId;

    public Notification() {
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
        getDataFromBundle();
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

    }
}