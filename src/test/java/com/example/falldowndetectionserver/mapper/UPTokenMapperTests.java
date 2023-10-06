package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.UPTokenVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class UPTokenMapperTests {
    @Autowired
    private UPTokenMapper uPTokenMapper;

    @Test
    public void insertTest() {
        uPTokenMapper.insert(UPTokenVO.builder().cameraId("cam01").token("testToken").build());
    }

//    @Test
//    public void selectTest() {
//        log.info(uPTokenMapper.select("cam01"));
//    }

    @Test
    public void deleteTest() {
        uPTokenMapper.delete("cam01");
    }
}
