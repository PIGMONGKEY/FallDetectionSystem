package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.NokPhoneVO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class NokPhoneDaoTests {
    @Autowired
    private NokPhoneDao nokPhoneDao;

    @Test
    public void insertTest() {
        NokPhoneVO nokPhoneVO = new NokPhoneVO();
        nokPhoneVO.setNokPhone("010-1234-1234");
        nokPhoneVO.setUno(4);

        nokPhoneDao.insert(nokPhoneVO);
    }

    @Test
    public void selectAllTest() {
        nokPhoneDao.selectAll(4);
    }

    @Test
    public void deleteTest() {
        nokPhoneDao.delete(4);
    }
}
