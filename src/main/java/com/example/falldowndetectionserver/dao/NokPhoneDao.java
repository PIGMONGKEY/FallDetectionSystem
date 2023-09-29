package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.NokPhoneVO;
import com.example.falldowndetectionserver.mapper.NokPhoneMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Component
@RequiredArgsConstructor
public class NokPhoneDao {
    private final NokPhoneMapper nokPhoneMapper;

    public int insert(NokPhoneVO nokPhoneVO) {
        try {
            return nokPhoneMapper.insert(nokPhoneVO);
        } catch (Exception e) {
            return -1;
        }
    }

    public int delete(String cameraId) {
        try {
            return nokPhoneMapper.delete(cameraId);
        } catch (Exception e) {
            return -1;
        }
    }

    public List<String> selectAll(String cameraId) {
        return nokPhoneMapper.selectAll(cameraId);
    }
}
