package com.example.falldowndetectionserver.mapper;

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
}
