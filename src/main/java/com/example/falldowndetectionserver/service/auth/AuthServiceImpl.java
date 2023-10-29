package com.example.falldowndetectionserver.service.auth;

import com.example.falldowndetectionserver.dao.UPTokenDao;
import com.example.falldowndetectionserver.domain.dto.BasicResponseDTO;
import com.example.falldowndetectionserver.domain.dto.user.LoginRequestDTO;
import com.example.falldowndetectionserver.domain.dto.auth.AuthTokenParam;
import com.example.falldowndetectionserver.domain.vo.UserPhoneTokenVO;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UPTokenDao uPTokenDao;

    /**
     * 로그인 서비스를 담당하는 메소드
     * @param loginRequestDTO cameraId와 password로 이루어진 LoginDTO를 파라미터로 받는다.
     * @return 로그인에 성공하면 token을 리턴하고, 실패하면 fail 문자열을 반환한다.
     */
    @Override
    public BasicResponseDTO<AuthTokenParam> login(LoginRequestDTO loginRequestDTO) {
        // 인증 토큰 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getCameraId(), loginRequestDTO.getPassword());
        try {
            // 인증 시도
            // 인증 실패 시 BadCredentialsException 발생
            Authentication authentication = authenticationManagerBuilder
                    .getObject()
                    .authenticate(authenticationToken);

            // 토큰 생성
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.createToken(authentication);

            // 핸드폰 FCM 토큰을 업데이트 한다.
            uPTokenDao.update(UserPhoneTokenVO.builder()
                    .cameraId(loginRequestDTO.getCameraId())
                    .token(loginRequestDTO.getDeviceToken())
                    .build());

            // 로그인 성공
            return BasicResponseDTO.<AuthTokenParam>builder()
                    .code(HttpStatus.OK.value())
                    .httpStatus(HttpStatus.OK)
                    .message("로그인 성공")
                    .data(AuthTokenParam.builder().token(jwt).build())
                    .build();
        } catch (BadCredentialsException e) {
            // 로그인 실패
            return BasicResponseDTO.<AuthTokenParam>builder()
                    .code(HttpStatus.UNAUTHORIZED.value())
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .message("사용자 인증 실패")
                    .build();
        }
    }

    /**
     * 로그아웃 서비스를 담당하는 서비스
     * @param authTokenParam token으로 이루어진 TokenDTO를 파라미터로 받는다.
     */
    @Override
    public void logout(AuthTokenParam authTokenParam) {
        // redis에 토큰을 저장한다.
        // 삭제 시간은 토큰 만료시간과 동일하게 하여 토큰이 만료되면 자동으로 redis에서 삭제되게 한다.
        redisTemplate.opsForValue().set(authTokenParam.getToken(), "logout", tokenProvider.getExpiration(authTokenParam.getToken()) - new Date().getTime(), TimeUnit.MILLISECONDS);
    }
}
