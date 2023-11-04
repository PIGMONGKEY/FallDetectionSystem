package com.example.falldetectionapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.falldetectionapp.register.RegisterActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 가이드를 보여주는 창입니다.
 * activity_guide.xml과 연결됩니다.
 */
public class GuideActivity extends AppCompatActivity {

    private ImageSlider guideSlider;
    private List<SlideModel> slideModels;
    private View decorView;
    private int uiOption;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        init();
    }

    //    초기 설정을 넣어주세요
    private void init() {
        setTitle("가이드");
        setView();
        hideUnderBar();
    }

    private void setView() {
        guideSlider = findViewById(R.id.slider_activity);

        slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.guide1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide5, ScaleTypes.FIT));

        guideSlider.setImageList(slideModels);
    }

    // 하단바 숨기기
    private void hideUnderBar() {
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT )
            uiOption |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        decorView.setSystemUiVisibility( uiOption );
    }
}