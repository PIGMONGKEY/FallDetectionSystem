package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.TestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface TestMapper {
    public TestDTO select();
}
