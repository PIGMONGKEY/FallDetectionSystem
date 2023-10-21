package com.example.falldowndetectionserver.service.user;

import com.example.falldowndetectionserver.dao.CameraIdDao;
import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenParam;
import com.example.falldowndetectionserver.domain.dto.user.SignUpRequestDTO;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.UserInfoRequestDTO;
import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;
    private final UPTokenDao uPTokenDao;
    private final PasswordEncoder passwordEncoder;
    private final CameraIdDao cameraIdDao;
    private final AuthService authService;

    @Override
    public BasicResponseDTO checkCameraId(String cameraId) {
        if (cameraIdDao.select(cameraId) == 1) {
            if (userDao.select(cameraId).isEmpty()) {
                return BasicResponseDTO.builder()
                        .code(HttpStatus.OK.value()) // 200
                        .httpStatus(HttpStatus.OK)
                        .message("등록 가능한 카메라 아이디 입니다.")
                        .build();
            } else {
                return BasicResponseDTO.builder()
                        .code(HttpStatus.FORBIDDEN.value()) // 403
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .message("이미 등록된 카메라 아이디 입니다.")
                        .build();
            }
        } else {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.NOT_FOUND.value()) //404
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("잘못된 카메라 아이디 입니다.")
                    .build();
        }
    }

    /**
     * 새로운 사용자를 등록하는 서비스
     * @param signUpRequestDTO signUpDTO 형태로 사용자 정보와 보호자 핸드폰 번호 까지 받는다.
     * @return 모두 성공하면 1, User실패는 -1, NokPhone 실패는 -2를 리턴한다.
     */
    @Override
    public BasicResponseDTO signup(SignUpRequestDTO signUpRequestDTO) {
        UserVO userVO = UserVO.builder()
                .cameraId(signUpRequestDTO.getUserInfo().getCameraId())
                .userPassword(passwordEncoder.encode(signUpRequestDTO.getUserInfo().getUserPassword()))
                .userName(signUpRequestDTO.getUserInfo().getUserName())
                .userAddress(signUpRequestDTO.getUserInfo().getUserAddress())
                .userPhone(signUpRequestDTO.getUserInfo().getUserPhone())
                .build();

        if (userDao.insert(userVO) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 정보 등록에 실패했습니다.")
                    .build();
        }

        NokPhoneVO nokPhoneVO = NokPhoneVO.builder()
                .cameraId(signUpRequestDTO.getUserInfo().getCameraId())
                .build();

        for (String nokPhone : signUpRequestDTO.getUserInfo().getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return BasicResponseDTO.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("보호자 연락처 정보 등록에 실패했습니다.")
                        .build();
            }
        }

        if (uPTokenDao.insert(signUpRequestDTO.getPhoneToken()) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 기기 정보 등록에 실패했습니다.")
                    .build();
        }

        return BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("사용자 등록을 성공했습니다.")
                .build();
    }

    /**
     * 사용자 정보를 불러오는 서비스
     * @param cameraId 카메라 아이디로 UserDao.select와 NokPhoneDao.selectAll을 호출한다.
     * @return 불러온 정보를 UserDTO 형태로 리턴한다.
     * userVO 또는 nokPhones가 null일 경우, UserDTO의 requestSuccess를 false로 설정하여 리턴한다.
     */
    @Override
    public BasicResponseDTO<UserInfoRequestDTO> getUserInfo(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElse(null);
        List<String> nokPhones = nokPhoneDao.selectAll(cameraId);

        if (userVO != null && !nokPhones.isEmpty()) {
            UserInfoRequestDTO userInfo = UserInfoRequestDTO.builder()
                    .cameraId(userVO.getCameraId())
                    .userPassword(userVO.getUserPassword())
                    .userName(userVO.getUserName())
                    .userAddress(userVO.getUserAddress())
                    .userPhone(userVO.getUserPhone())
                    .nokPhones(nokPhones)
                    .build();

            return BasicResponseDTO.<UserInfoRequestDTO>builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("사용자 정보 조회 성공")
                    .data(userInfo)
                    .build();
        } else {
            return BasicResponseDTO.<UserInfoRequestDTO>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .message("사용자 정보가 존재하지 않습니다.")
                    .build();
        }
    }

    /**
     * 사용자를 삭제하는 서비스
     * @param cameraId 카메라 아이디로 구별한다.
     * @return 삭제 성공 시엔 1을 리턴한다.
     */
    @Override
    public BasicResponseDTO removeUserInfo(String cameraId, String token) {
        if (userDao.delete(cameraId) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 삭제에 실패했습니다.")
                    .build();
        } else {
            // 토큰을 만료 시킨다.
            authService.logout(AuthTokenParam.builder().token(token).build());
            return BasicResponseDTO.builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("사용자 삭제 성공")
                    .build();
        }
    }

    /**
     * 사용자 정보를 수정한다.
     * @param userInfoRequestDTO UserDTO 형태로 모든 정보를 받아와서 update를 해준다. - NokPhone은 삭제 후 다시 삽입한다.
     * @return 삭제 성공시 1, User 실패시 -1, NokPhone 실패시 -2 리턴한다.
     */
    @Override
    public BasicResponseDTO modifyUserInfo(UserInfoRequestDTO userInfoRequestDTO) {
        UserVO userVO;
        // 비밀번호가 변경됨!
        if (userDao.select(userInfoRequestDTO.getCameraId()).get().getUserPassword() != userInfoRequestDTO.getUserPassword()) {
            userVO = UserVO.builder()
                    .cameraId(userInfoRequestDTO.getCameraId())
                    .userPassword(passwordEncoder.encode(userInfoRequestDTO.getUserPassword()))
                    .userName(userInfoRequestDTO.getUserName())
                    .userAddress(userInfoRequestDTO.getUserAddress())
                    .userPhone(userInfoRequestDTO.getUserPhone())
                    .build();
        } else {
            userVO = UserVO.builder()
                    .cameraId(userInfoRequestDTO.getCameraId())
                    .userPassword(userInfoRequestDTO.getUserPassword())
                    .userName(userInfoRequestDTO.getUserName())
                    .userAddress(userInfoRequestDTO.getUserAddress())
                    .userPhone(userInfoRequestDTO.getUserPhone())
                    .build();
        }

        if (userDao.update(userVO) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 정보 수정에 실패했습니다.")
                    .build();
        }

        nokPhoneDao.delete(userInfoRequestDTO.getCameraId());
        NokPhoneVO nokPhoneVO = NokPhoneVO.builder().cameraId(userInfoRequestDTO.getCameraId()).build();

        for (String nokPhone : userInfoRequestDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return BasicResponseDTO.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("보호자 연락처 정보 수정에 실패했습니다.")
                        .build();
            }
        }

        return BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("사용자 정보 수정에 성공했습니다.")
                .build();
    }
}
