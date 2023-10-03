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
     *
     * @param loginDTO
     * @return
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

    @Override
    public void logout(TokenDTO tokenDTO) {
        redisTemplate.opsForValue().set(tokenDTO.getToken(), "logout", tokenProvider.getExpiration(tokenDTO.getToken()) - new Date().getTime(), TimeUnit.MILLISECONDS);
    }
}
