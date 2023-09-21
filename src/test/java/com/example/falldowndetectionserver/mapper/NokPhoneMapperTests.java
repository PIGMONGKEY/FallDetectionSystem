package com.example.falldowndetectionserver.mapper;

import com.example.falldowndetectionserver.domain.NOKPhoneVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class NokPhoneMapperTests {
    @Autowired
    private NOKPhoneMapper nokPhoneMapper;

    @Test
    public void insertTest() {
        NOKPhoneVO nokPhoneVO = new NOKPhoneVO();
        nokPhoneVO.setNokPhone("010-1234-1234");
        nokPhoneVO.setUno(4);

        nokPhoneMapper.insert(nokPhoneVO);
    }

    @Test
    public void selectTest() {
        nokPhoneMapper.selectAll(4);
    }

    @Test
    public void deleteTest() {
        nokPhoneMapper.delete(4);
    }
}
