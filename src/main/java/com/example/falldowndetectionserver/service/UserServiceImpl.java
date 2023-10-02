package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.dao.NokPhoneDao;
import com.example.falldowndetectionserver.dao.UserDao;
import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.TokenDTO;
import com.example.falldowndetectionserver.domain.vo.NokPhoneVO;
import com.example.falldowndetectionserver.domain.dto.UserDTO;
import com.example.falldowndetectionserver.domain.vo.UserVO;
import com.example.falldowndetectionserver.jwt.JwtFilter;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final NokPhoneDao nokPhoneDao;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 새로운 사용자를 등록하는 서비스
     * @param userDTO userDTO 형태로 사용자 정보와 보호자 핸드폰 번호 까지 받는다.
     * @return 모두 성공하면 1, User실패는 -1, NokPhone 실패는 -2를 리턴한다.
     */
    @Override
    public String signup(UserDTO userDTO) {
        if (!userDao.select(userDTO.getCameraId()).isEmpty()) {
            return "Registered CameraId";
        }

        UserVO userVO = UserVO.builder()
                .cameraId(userDTO.getCameraId())
                .userPassword(passwordEncoder.encode(userDTO.getUserPassword()))
                .userName(userDTO.getUserName())
                .userAddress(userDTO.getUserAddress())
                .userPhone(userDTO.getUserPhone())
                .build();

        if (userDao.insert(userVO) != 1) {
            return "User Register Error";
        }

        NokPhoneVO nokPhoneVO = NokPhoneVO.builder()
                .cameraId(userDTO.getCameraId())
                .build();

        for (String nokPhone : userDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return "NokPhone Register Error";
            }
        }

        return "Success";
    }

    @Override
    public TokenDTO login(LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDTO.getCameraId(), loginDTO.getPassword());
        try {
            Authentication authentication = authenticationManagerBuilder
                    .getObject()
                    .authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);

            return TokenDTO.builder()
                    .token(jwt)
                    .build();
//            TokenDTO tokenDTO = new TokenDTO();
//            tokenDTO.setToken(jwt);
//            return tokenDTO;
        } catch (BadCredentialsException e) {
            return TokenDTO.builder()
                    .token("fail")
                    .build();
//            TokenDTO tokenDTO = new TokenDTO();
//            tokenDTO.setToken("fail");
//            return tokenDTO;
        }
    }

    @Override
    public void logout(TokenDTO tokenDTO) {
        redisTemplate.opsForValue().set(tokenDTO.getToken(), "logout", tokenProvider.getExpiration(tokenDTO.getToken()) - new Date().getTime(), TimeUnit.MILLISECONDS);
    }

    /**
     * 사용자 정보를 불러오는 서비스
     * @param cameraId 카메라 아이디로 UserDao.select와 NokPhoneDao.selectAll을 호출한다.
     * @return 불러온 정보를 UserDTO 형태로 리턴한다.
     * userVO 또는 nokPhones가 null일 경우, UserDTO의 requestSuccess를 false로 설정하여 리턴한다.
     */
    @Override
    public UserDTO getUserInfo(String cameraId) {
        UserVO userVO = userDao.select(cameraId).orElse(null);
        List<String> nokPhones = nokPhoneDao.selectAll(cameraId);
        UserDTO userDTO;

        if (userVO != null && !nokPhones.isEmpty()) {
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
                    .nokPhones(nokPhones)
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
    public String removeUserInfo(String cameraId) {
        if (userDao.delete(cameraId) != 1) {
            return "Fail";
        } else {
            return "Success";
        }
    }

    /**
     * 사용자 정보를 수정한다.
     * @param userDTO UserDTO 형태로 모든 정보를 받아와서 update를 해준다. - NokPhone은 삭제 후 다시 삽입한다.
     * @return 삭제 성공시 1, User 실패시 -1, NokPhone 실패시 -2 리턴한다.
     */
    @Override
    public String modifyUserInfo(UserDTO userDTO) {
        UserVO userVO = UserVO.builder()
                .cameraId(userDTO.getCameraId())
                .userPassword(passwordEncoder.encode(userDTO.getUserPassword()))
                .userName(userDTO.getUserName())
                .userAddress(userDTO.getUserAddress())
                .userPhone(userDTO.getUserPhone())
                .build();

        if (userDao.update(userVO) != 1) {
            return "User Update Fail";
        }

        nokPhoneDao.delete(userDTO.getCameraId());
        NokPhoneVO nokPhoneVO = NokPhoneVO.builder().cameraId(userDTO.getCameraId()).build();

        for (String nokPhone : userDTO.getNokPhones()) {
            nokPhoneVO.setNokPhone(nokPhone);
            if (nokPhoneDao.insert(nokPhoneVO) != 1) {
                return "NokPhone Update Fail";
            }
        }

        return "Success";
    }
}
