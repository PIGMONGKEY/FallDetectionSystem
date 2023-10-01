package com.example.falldowndetectionserver.dao;

import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
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
        nokPhoneVO.setCameraId("cam01");

        nokPhoneDao.insert(nokPhoneVO);
    }

    @Test
    public void selectAllTest() {
        nokPhoneDao.selectAll("cam01");
    }

    @Test
    public void deleteTest() {
        nokPhoneDao.delete("cam01");
    }
}
