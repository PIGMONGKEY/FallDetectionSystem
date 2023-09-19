package com.example.falldowndetectionserver.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class TestDTO {
    private String testId;
    private String testName;
}
