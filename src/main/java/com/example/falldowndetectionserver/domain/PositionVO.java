package com.example.falldowndetectionserver.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PositionVO {
    private int min_x;
    private int min_y;
    private int max_x;
    private int max_y;
}
