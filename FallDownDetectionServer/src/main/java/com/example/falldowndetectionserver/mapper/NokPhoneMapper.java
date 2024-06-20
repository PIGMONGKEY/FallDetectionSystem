package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NokPhoneMapper {
    public int insert(NokPhoneVO nokPhoneVO);
    public int updateToken(NokPhoneVO nokPhoneVO);
    public int delete(String cameraId);
    public List<NokPhoneVO> selectAll(String cameraId);
}
