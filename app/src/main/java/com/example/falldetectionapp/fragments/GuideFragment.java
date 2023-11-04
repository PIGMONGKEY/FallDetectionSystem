package com.example.falldetectionapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.falldetectionapp.GuideActivity;
import com.example.falldetectionapp.HomeActivity;
import com.example.falldetectionapp.R;

import java.util.ArrayList;
import java.util.List;

public class GuideFragment extends Fragment {

    private ImageButton backButton;
    private ImageSlider guideSlider;

    private List<SlideModel> slideModels;

    public GuideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guide, container, false);
        init(view);

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void init(View view) {
        getActivity().setTitle("가이드");
        setView(view);
    }

    private void setView(View view) {
        backButton = view.findViewById(R.id.imageButton4);
        guideSlider = view.findViewById(R.id.slider_fragment);

        slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.guide1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.guide5, ScaleTypes.FIT));

        guideSlider.setImageList(slideModels);
    }

}