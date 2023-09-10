package com.example.falldowndetectionserver.controller;

import lombok.Getter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VideoController {
    @GetMapping("/show")
    public String showVideo() {
        return "show-video";
    }
}
