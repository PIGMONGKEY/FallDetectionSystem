package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 사용자를 등록하는 서비스
     * @param userDTO userDTO 형태로 사용자 정보와 보호자 핸드폰 번호 까지 받는다.
     * @return 모두 성공하면 1, User실패는 -1, NokPhone 실패는 -2를 리턴한다.
     */
    @Override
    public String signup(UserDTO userDTO) {
        UserVO userVO = new UserVO();

        userVO.setCameraId(userDTO.getCameraId());
        userVO.setUserPassword(passwordEncoder.encode(userDTO.getUserPassword()));
        userVO.setUserName(userDTO.getUserName());
        userVO.setUserPhone(userDTO.getUserPhone());
        userVO.setUserAddress(userDTO.getUserAddress());

        if (userDao.select(userVO.getCameraId()).orElseGet(null) != null) {
            return "Registered CameraId";
        }

        if (userDao.insert(userVO) != 1) {
            return "User Register Error";
        }

        NokPhoneVO nokPhoneVO = new NokPhoneVO();
        nokPhoneVO.setCameraId(userDTO.getCameraId());
        for (String nokPhone : userDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return "NokPhone Register Error";
            }
        }

        return "Success";
    }

    /**
     * 사용자 정보를 불러오는 서비스
     * @param cameraId 카메라 아이디로 불러온다.
     * @return 불러온 정보를 UserDTO 형태로 리턴한다.
     */
    @Override
    public UserDTO getUserInfo(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElse(null);
        UserDTO userDTO;

        if (userVO != null) {
            userDTO = UserDTO.builder()
                    .requestSuccess(true)
                    .cameraId(userVO.getCameraId())
                    .userPassword(userVO.getUserPassword())
                    .userName(userVO.getUserName())
                    .userAddress(userVO.getUserAddress())
                    .userPhone(userVO.getUserPhone())
                    .userRole(userVO.getUserRole())
                    .regdate(userVO.getRegdate())
                    .updatedate(userVO.getUpdatedate())
                    .nokPhones(nokPhoneDao.selectAll(cameraId))
                    .build();
        } else {
            userDTO = UserDTO.builder()
                    .requestSuccess(false)
                    .build();
        }

        return userDTO;
    }

    /**
     * 사용자를 삭제하는 서비스
     * @param cameraId 카메라 아이디로 구별한다.
     * @return 삭제 성공 시엔 1을 리턴한다.
     */
    @Override
    public int removeUserInfo(String cameraId) {
        return userDao.delete(cameraId);
    }

    /**
     * 사용자 정보를 수정한다.
     * @param userDTO UserDTO 형태로 모든 정보를 받아와서 update를 해준다. - NokPhone은 삭제 후 다시 삽입한다.
     * @return 삭제 성공시 1, User 실패시 -1, NokPhone 실패시 -2 리턴한다.
     */
    @Override
    public int modifyUserInfo(UserDTO userDTO) {
        UserVO userVO = new UserVO();
        NokPhoneVO nokPhoneVO = new NokPhoneVO();

        userVO.setCameraId(userDTO.getCameraId());
        userVO.setUserPassword(userDTO.getUserPassword());
        userVO.setUserName(userDTO.getUserName());
        userVO.setUserPhone(userDTO.getUserPhone());
        userVO.setUserAddress(userDTO.getUserAddress());

        if (userDao.update(userVO) != 1) {
            return -1;
        }

        nokPhoneDao.delete(userDTO.getCameraId());
        nokPhoneVO.setCameraId(userDTO.getCameraId());
        for (String nokPhone : userDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return -2;
            }
        }

        return 1;
    }
}
