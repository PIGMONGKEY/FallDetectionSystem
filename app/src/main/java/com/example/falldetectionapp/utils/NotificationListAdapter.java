package com.example.falldetectionapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.falldetectionapp.DTO.NotificationDTO;
import com.example.falldetectionapp.R;

import java.util.ArrayList;

public class NotificationListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NotificationDTO> notificationDTOS = new ArrayList<>();

    public NotificationListAdapter(Context context, ArrayList<NotificationDTO> notificationDTOS) {
        this.context = context;
        this.notificationDTOS = notificationDTOS;
    }

    @Override
    public int getCount() {
        return notificationDTOS.size();
    }

    @Override
    public NotificationDTO getItem(int position) {
        return notificationDTOS.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.notification_item, parent, false);

        TextView title, regdate;

        title = convertView.findViewById(R.id.titleTextView_notification_item);
        regdate = convertView.findViewById(R.id.regdateTextView_notification_item);

        title.setText(getItem(position).getTitle());
        regdate.setText(getItem(position).getRegdate());

        return convertView;
    }
}
