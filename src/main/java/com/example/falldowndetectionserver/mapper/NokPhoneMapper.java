package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.NokPhoneVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface NokPhoneMapper {
    public void insert(NokPhoneVO nokPhoneVO);
    public void delete(int uno);
    public List<String> selectAll(int uno);
}
