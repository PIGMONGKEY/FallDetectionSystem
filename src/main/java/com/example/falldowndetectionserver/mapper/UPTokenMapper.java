package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Mapper
@Component
public interface UPTokenMapper {
    public int insert(UserPhoneTokenVO userPhoneTokenVO);
    public Optional<String> select(String cameraId);
    public int delete(String cameraId);
}
