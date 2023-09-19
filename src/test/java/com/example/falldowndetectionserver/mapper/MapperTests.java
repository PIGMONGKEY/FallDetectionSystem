package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.TestDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class MapperTests {
    @Autowired
    private TestMapper testMapper;

    @Test
    public void selectTest() {
        log.info(testMapper.select().toString());
    }

    @Test
    public void insertTest() {
        TestDTO testDTO = new TestDTO();
        testDTO.setTestId("test");
        testDTO.setTestName("test");
        testMapper.insert(testDTO);
    }
}
