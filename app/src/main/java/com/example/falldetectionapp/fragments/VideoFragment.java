package com.example.falldetectionapp.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import com.example.falldetectionapp.BuildConfig;
import com.example.falldetectionapp.DTO.BasicResponseDTO;
import com.example.falldetectionapp.R;
import com.example.falldetectionapp.retrofit.VideoService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

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

    private VideoView videoView;
    private String personalToken, cameraId;

//    빈 생성자가 있어야 합니다. 삭제하면 안됩니다.
    public VideoFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        setView(view);
        setListeners();
        getDataFromBundle();
        requestStreamingURL();
    }

    private void setView(View view) {
        videoView = view.findViewById(R.id.videoImageView);
    }

    private void getDataFromBundle() {
        personalToken = getArguments().getString("personalToken");
        cameraId = getArguments().getString("cameraId");
    }

    private void requestStreamingURL() {
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL) // 기본으로 적용되는 서버 URL (반드시 / 로 마무리되게 설정)
                .addConverterFactory(GsonConverterFactory.create(gson)) // JSON 데이터를 Gson 라이브러리로 파싱하고 데이터를 Model에 자동으로 담는 converter
                .build();

        VideoService videoService = retrofit.create(VideoService.class);

        videoService.requestStreamingUrl("Bearer " + personalToken, cameraId).enqueue(new Callback<BasicResponseDTO<String>>() {
            @Override
            public void onResponse(Call<BasicResponseDTO<String>> call, Response<BasicResponseDTO<String>> response) {
                if (response.isSuccessful()) {
                    videoView.setVideoURI(Uri.parse(response.body().getData().trim()));
                } else {

                }
            }

            @Override
            public void onFailure(Call<BasicResponseDTO<String>> call, Throwable t) {

            }
        });
    }

    private void setListeners() {
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                videoView.start();
            }
        });
    }

}