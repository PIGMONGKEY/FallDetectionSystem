package com.example.falldowndetectionserver.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PositionVO {
    public static final String[] keypoint = {   "nose",
                                                "left eye",
                                                "right eye",
                                                "left ear",
                                                "right ear",
                                                "left shoulder",
                                                "right shoulder",
                                                "left elbow",
                                                "right elbow",
                                                "left wrist",
                                                "right wrist",
                                                "left hip",
                                                "right hip",
                                                "left knee",
                                                "right knee",
                                                "left ankle",
                                                "right ankle"
                                            };

    private double[][] position;
    private int min_x;
    private int min_y;
    private int max_x;
    private int max_y;
}
