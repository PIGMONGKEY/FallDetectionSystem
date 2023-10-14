package com.example.falldowndetectionserver.service.emergency;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmergencyServiceImpl implements EmergencyService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;

    @Override
    public void sendSMS(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElseThrow();
        List<String> nokPhones = nokPhoneDao.selectAll(cameraId);

        String address = userVO.getUserAddress();
        String phone = userVO.getUserPhone();
        String name = userVO.getUserName();


    }
}
