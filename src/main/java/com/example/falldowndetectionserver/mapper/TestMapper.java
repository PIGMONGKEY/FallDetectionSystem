package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.TestDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface TestMapper {
    public List<TestDTO> select();
    public void insert(TestDTO testDTO);
}
