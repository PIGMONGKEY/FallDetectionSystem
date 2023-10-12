package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.mapper.CameraIdMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Component
public class CameraIdDao {
    private final CameraIdMapper cameraIdMapper;

    public int select(String cameraId) {
        // 이미 있으면 1, 없으면 0 리턴
        if (cameraIdMapper.select(cameraId) != 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
