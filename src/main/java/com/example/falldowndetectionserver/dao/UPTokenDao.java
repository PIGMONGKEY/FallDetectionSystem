package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import com.example.falldowndetectionserver.mapper.UPTokenMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Component
@RequiredArgsConstructor
public class UPTokenDao {
    private final UPTokenMapper uPTokenMapper;

    public int insert(UserPhoneTokenVO uPTokenVO) {
        try {
            return uPTokenMapper.insert(uPTokenVO);
        } catch (Exception e) {
            return -1;
        }
    }

    public Optional<String> select(String cameraId) {
        return uPTokenMapper.select(cameraId);
    }

    public int delete(String cameraId) {
        try {
            return uPTokenMapper.delete(cameraId);
        } catch (Exception e) {
            return -1;
        }
    }
}
