package com.example.falldowndetectionserver.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 라즈베리 파이 카메라 영상 확인을 위한 테스트 page
 */
@Controller
public class VideoController {
    @GetMapping("/show")
    public String showVideo() {
        return "show-video";
    }
}
