package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
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
    public void registerUserInfo(UserVO userVO) {
        userDao.insert(userVO);
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
    public void modifyUserInfo(UserVO userVO) {
        userDao.update(userVO);
    }
}
