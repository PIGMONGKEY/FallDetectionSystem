package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.NokPhoneVO;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

public interface NokPhoneService {
    public void registerNokPhone(NokPhoneVO nokPhoneVO);
    public void removeNokPhone(int uno);
    public List<NokPhoneVO> getAllNokPhone(int uno);
}
