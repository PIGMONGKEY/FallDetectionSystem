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

    /**
     * 카메라 아이디의 존재 유무를 확인한다.
     */
    public int select(String cameraId) {
        // 있으면 1, 없으면 0 리턴
        return cameraIdMapper.select(cameraId);
    }
}
