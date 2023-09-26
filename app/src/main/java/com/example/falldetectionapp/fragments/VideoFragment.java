package com.example.falldetectionapp.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.falldetectionapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * HomeActivity의 FrameLayout에 들어가는 VideoFragment입니다.
 * 화면 안에 들어가는 화면이라고 생각하시면 됩니다.
 * 하단 네비게이션 바를 클릭하면 다른 화면으로 바뀝니다.
 * 위 로직은 HomeActivity에서 구현되어있습니다.
 *
 * Java 코드를 작성하실 때, Fragment에 대해 조금 찾아보시면서 작성하셔야 할 듯 합니다.
 * Activity와는 다르게 독자적으로 존재할 수 없는 화면이고, 화면 안에 있는 화면이다보니, 로직을 짜는 방식이 조금 다를껍니다.
 */
public class VideoFragment extends Fragment {

    private WebSocket socket;
    private final JSONObject CONNECTION_INFO = new JSONObject();
    private ImageView videoImageView;

//    빈 생성자가 있어야 합니다. 삭제하면 안됩니다.
    public VideoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        setView(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

//        비디오 출력은 주석 처리해놓겠습니다.
//        init();
    }

    @Override
    public void onPause() {
        super.onPause();

        socket.close(1000, "close");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        socket.close(1000, "close");
    }

    private void init() {
        try {
            videoReceive();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setView(View view) {
        videoImageView = view.findViewById(R.id.videoImageView);
    }

    private void videoReceive() throws InterruptedException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("ws://10.0.2.2:10000/video").build();

        socket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d("video", "closed");
            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                Log.d("video", "closing");
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                Log.d("video", "failure : " + t.getCause() + " / " + t.getMessage() + " / " + t);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                Log.d("video", "get message : " + text);
            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showImage(BitmapFactory.decodeByteArray(bytes.toByteArray(), 0, bytes.toByteArray().length));
                    }
                });
            }

            @Override
            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                Log.d("video", "connected");
                try {
                    CONNECTION_INFO.put("identifier", "receiver");
                    CONNECTION_INFO.put("camera_id", "cam01");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                webSocket.send(CONNECTION_INFO.toString());
            }
        });
    }


    private void showImage(Bitmap bitmap) {
        videoImageView.setImageBitmap(bitmap);
    }
}