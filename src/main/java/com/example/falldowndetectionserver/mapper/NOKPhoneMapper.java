package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.NOKPhoneVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NOKPhoneMapper {
    public void insert(NOKPhoneVO nokPhoneVO);
    public void delete(int uno);
    public List<NOKPhoneVO> selectAll(int uno);
}
