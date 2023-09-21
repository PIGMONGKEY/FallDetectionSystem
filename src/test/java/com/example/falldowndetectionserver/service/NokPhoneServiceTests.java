package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.NokPhoneVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class NokPhoneServiceTests {
    @Autowired
    private NokPhoneService nokPhoneService;

    @Test
    public void registerNokPhone() {
        NokPhoneVO nokPhoneVO = new NokPhoneVO();
        nokPhoneVO.setNokPhone("010-1234-1234");
        nokPhoneVO.setUno(4);

        nokPhoneService.registerNokPhone(nokPhoneVO);
    }

    @Test
    public void getAllNokPhoneTest() {
        nokPhoneService.getAllNokPhone(4);
    }

    @Test
    public void removeNokPhoneTest() {
        nokPhoneService.removeNokPhone(4);
    }
}
