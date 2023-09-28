package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.NokPhoneVO;
import com.example.falldowndetectionserver.domain.UserDTO;
import com.example.falldowndetectionserver.domain.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;

    @Override
    public void registerUserInfo(UserDTO userDTO) {
        UserVO userVO = new UserVO();

        userVO.setCameraId(userDTO.getCameraId());
        userVO.setUserPassword(userDTO.getUserPassword());
        userVO.setUserName(userDTO.getUserName());
        userVO.setUserPhone(userDTO.getUserPhone());
        userVO.setUserAddress(userDTO.getUserAddress());

        userDao.insert(userVO);

        NokPhoneVO nokPhoneVO = new NokPhoneVO();
        nokPhoneVO.setCameraId(userDTO.getCameraId());
        for (String nokPhone : userDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            nokPhoneDao.insert(nokPhoneVO);
        }
    }

    @Override
    public UserDTO getUserInfo(int uno) {
        UserVO userVO = userDao.select(uno);
        UserDTO userDTO = new UserDTO();

        userDTO.setUno(uno);
        userDTO.setCameraId(userVO.getCameraId());
        userDTO.setUserPassword(userVO.getUserPassword());
        userDTO.setUserName(userVO.getUserName());
        userDTO.setUserAddress(userVO.getUserAddress());
        userDTO.setUserPhone(userVO.getUserPhone());
        userDTO.setRegdate(userVO.getRegdate());
        userDTO.setUpdatedate(userVO.getUpdatedate());
        userDTO.setNokPhones(nokPhoneDao.selectAll(userVO.getCameraId()));

        return userDTO;
    }

    @Override
    public void removeUserInfo(int uno) {
        userDao.delete(uno);
    }

    @Override
    public void modifyUserInfo(UserDTO userDTO) {
        UserVO userVO = new UserVO();
        NokPhoneVO nokPhoneVO = new NokPhoneVO();

        userVO.setUno(userDTO.getUno());
        userVO.setCameraId(userDTO.getCameraId());
        userVO.setUserPassword(userDTO.getUserPassword());
        userVO.setUserName(userDTO.getUserName());
        userVO.setUserPhone(userDTO.getUserPhone());
        userVO.setUserAddress(userDTO.getUserAddress());

        userDao.update(userVO);

        nokPhoneDao.delete(userDTO.getCameraId());
        nokPhoneVO.setCameraId(userDTO.getCameraId());
        for (String nokPhone : userDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            nokPhoneDao.insert(nokPhoneVO);
        }
    }
}
