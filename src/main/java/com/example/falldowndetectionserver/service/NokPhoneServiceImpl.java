package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.domain.NokPhoneVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NokPhoneServiceImpl implements NokPhoneService{
    private final NokPhoneDao nokPhoneDao;

    @Override
    public void registerNokPhone(NokPhoneVO nokPhoneVO) {
        nokPhoneDao.insert(nokPhoneVO);
    }

    @Override
    public void removeNokPhone(int uno) {
        nokPhoneDao.delete(uno);
    }

    @Override
    public List<String> getAllNokPhone(int uno) {
        return nokPhoneDao.selectAll(uno);
    }
}
