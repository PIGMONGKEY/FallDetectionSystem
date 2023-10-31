package com.example.falldetectionapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.falldetectionapp.R;

public class NotificationInfoFragment extends Fragment {

    private TextView titleTV, contentTV, regdateTV, updatedateTV;

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
    }

    private void setView(View view) {
        titleTV = view.findViewById(R.id.titleTextView_notification_info);
        contentTV = view.findViewById(R.id.contentTextView_notification_info);
        regdateTV = view.findViewById(R.id.regdateTextView_notification_info);
        updatedateTV = view.findViewById(R.id.updatedateTextView_notification_info);
    }

    private void requestNotificationInfo() {

    }

    private void showNotificationContent() {

    }
}