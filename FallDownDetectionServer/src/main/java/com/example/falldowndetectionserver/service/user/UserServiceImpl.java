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

import java.util.ArrayList;
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

    /**
     * 가입 가능한 cameraId 인지 확인하는 서비스
     */
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
        // DTO로부터 VO를 생성한다.
        UserVO userVO = UserVO.builder()
                .cameraId(signUpRequestDTO.getUserInfo().getCameraId())
                .userPassword(passwordEncoder.encode(signUpRequestDTO.getUserInfo().getUserPassword()))
                .userName(signUpRequestDTO.getUserInfo().getUserName())
                .userAge(signUpRequestDTO.getUserInfo().getUserAge())
                .userGender(signUpRequestDTO.getUserInfo().getUserGender())
                .userBloodType(signUpRequestDTO.getUserInfo().getUserBloodType())
                .userAddress(signUpRequestDTO.getUserInfo().getUserAddress())
                .userPhone(signUpRequestDTO.getUserInfo().getUserPhone())
                .build();

        // 사용자 정보 저장 실패
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

        // 보호자 연락처 저장
        for (String nokPhone : signUpRequestDTO.getUserInfo().getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);

            // 보호자 연락처 저장 실패
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return BasicResponseDTO.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("보호자 연락처 정보 등록에 실패했습니다.")
                        .build();
            }
        }

        // 핸드폰 고유 토큰 저장 실패
        if (uPTokenDao.insert(signUpRequestDTO.getPhoneToken()) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 기기 정보 등록에 실패했습니다.")
                    .build();
        }

        // 회원가입 성공
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
        List<NokPhoneVO> nokPhoneVOs = nokPhoneDao.selectAll(cameraId);

        List<String> nokPhones = new ArrayList<>();

        for (NokPhoneVO nokPhoneVO : nokPhoneVOs) {
            nokPhones.add(nokPhoneVO.getNokPhone());
        }

        // 사용자 정보가 있고 보호자 연락처가 등록되어 있다면 UserInfoDTO 생성하여 리턴
        if (userVO != null && !nokPhones.isEmpty()) {
            UserInfoRequestDTO userInfo = UserInfoRequestDTO.builder()
                    .cameraId(userVO.getCameraId())
                    .userPassword(userVO.getUserPassword())
                    .userName(userVO.getUserName())
                    .userAge(userVO.getUserAge())
                    .userGender(userVO.getUserGender())
                    .userBloodType(userVO.getUserBloodType())
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
        // 사용자 정보가 없다면 404 리턴
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
        // 삭제 실패
        if (userDao.delete(cameraId) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 삭제에 실패했습니다.")
                    .build();
        } else {
        // 삭제 성공
            // 토큰을 만료 시킨다.
            authService.logout(AuthTokenParam.builder().token(token.substring(7)).build());
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
        String oldPassword = userDao.select(userInfoRequestDTO.getCameraId()).get().getUserPassword();
        String newPassword = userInfoRequestDTO.getUserPassword();
        // 비밀번호가 변경된 경우
        if (!oldPassword.equals(newPassword)) {
            userVO = UserVO.builder()
                    .cameraId(userInfoRequestDTO.getCameraId())
                    .userPassword(passwordEncoder.encode(userInfoRequestDTO.getUserPassword()))
                    .userName(userInfoRequestDTO.getUserName())
                    .userAge(userInfoRequestDTO.getUserAge())
                    .userGender(userInfoRequestDTO.getUserGender())
                    .userBloodType(userInfoRequestDTO.getUserBloodType())
                    .userAddress(userInfoRequestDTO.getUserAddress())
                    .userPhone(userInfoRequestDTO.getUserPhone())
                    .build();
        } else {
        // 비밀번호가 변경되지 않은 경우
            userVO = UserVO.builder()
                    .cameraId(userInfoRequestDTO.getCameraId())
                    .userPassword(userInfoRequestDTO.getUserPassword())
                    .userName(userInfoRequestDTO.getUserName())
                    .userAge(userInfoRequestDTO.getUserAge())
                    .userGender(userInfoRequestDTO.getUserGender())
                    .userBloodType(userInfoRequestDTO.getUserBloodType())
                    .userAddress(userInfoRequestDTO.getUserAddress())
                    .userPhone(userInfoRequestDTO.getUserPhone())
                    .build();
        }

        // 사용자 정보 수정 실패 - 500 리턴
        if (userDao.update(userVO) != 1) {
            return BasicResponseDTO.builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .message("사용자 정보 수정에 실패했습니다.")
                    .build();
        }

        // 보호자 연락처 정보 모두 삭제
        nokPhoneDao.delete(userInfoRequestDTO.getCameraId());
        NokPhoneVO nokPhoneVO = NokPhoneVO.builder().cameraId(userInfoRequestDTO.getCameraId()).build();

        // 보호자 연락처 정보 다시 삽입
        for (String nokPhone : userInfoRequestDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);

            // 보호자 연락처 정보 삽입 실패 - 500 리턴
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return BasicResponseDTO.builder()
                        .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                        .message("보호자 연락처 정보 수정에 실패했습니다.")
                        .build();
            }
        }

        // 사용자 정보 수정 성공 - 200 리턴
        return BasicResponseDTO.builder()
                .code(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .message("사용자 정보 수정에 성공했습니다.")
                .build();
    }
}
