package com.example.falldowndetectionserver.domain.vo;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class PositionVO {
    private int min_x;
    private int min_y;
    private int max_x;
    private int max_y;
}
