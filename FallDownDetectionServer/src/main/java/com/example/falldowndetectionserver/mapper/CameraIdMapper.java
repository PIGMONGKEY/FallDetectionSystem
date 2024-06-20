package com.example.falldowndetectionserver.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CameraIdMapper {
    public int select(String cameraId);
}
