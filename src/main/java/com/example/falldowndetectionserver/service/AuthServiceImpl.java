package com.example.falldowndetectionserver.service;

import com.example.falldowndetectionserver.domain.dto.LoginDTO;
import com.example.falldowndetectionserver.domain.dto.TokenDTO;
import com.example.falldowndetectionserver.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

    /**
     * 로그인 서비스를 담당하는 메소드
     * @param loginDTO cameraId와 password로 이루어진 LoginDTO를 파라미터로 받는다.
     * @return 로그인에 성공하면 token을 리턴하고, 실패하면 fail 문자열을 반환한다.
     */
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
        } catch (BadCredentialsException e) {
            return TokenDTO.builder()
                    .token("fail")
                    .build();
        }
    }

    /**
     * 로그아웃 서비스를 담당하는 서비스
     * @param tokenDTO token으로 이루어진 TokenDTO를 파라미터로 받는다.
     */
    @Override
    public void logout(TokenDTO tokenDTO) {
        redisTemplate.opsForValue().set(tokenDTO.getToken(), "logout", tokenProvider.getExpiration(tokenDTO.getToken()) - new Date().getTime(), TimeUnit.MILLISECONDS);
    }
}
