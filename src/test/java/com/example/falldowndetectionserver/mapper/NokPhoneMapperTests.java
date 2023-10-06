package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class NokPhoneMapperTests {
    @Autowired
    private NokPhoneMapper nokPhoneMapper;

//    @Test
//    public void insertTest() {
//        NokPhoneVO nokPhoneVO = new NokPhoneVO();
//        nokPhoneVO.setNokPhone("010-1234-1234");
//        nokPhoneVO.setCameraId("cam01");
//
//        nokPhoneMapper.insert(nokPhoneVO);
//    }

    @Test
    public void selectTest() {
        nokPhoneMapper.selectAll("cam01");
    }

    @Test
    public void deleteTest() {
        nokPhoneMapper.delete("cam01");
    }
}
