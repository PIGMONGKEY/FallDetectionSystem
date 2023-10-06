package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.UPTokenVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UPTokenMapper {
    public int insert(UPTokenVO upTokenVO);
    public String select(String cameraId);
    public int delete(String cameraId);
}
