package com.example.falldowndetectionserver.config;

import com.example.falldowndetectionserver.jwt.JwtAccessDeniedHandler;
import com.example.falldowndetectionserver.jwt.JwtAuthenticationEntryPoint;
import com.example.falldowndetectionserver.jwt.JwtFilter;
import com.example.falldowndetectionserver.jwt.TokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * SpringSecurity 설정 파일
 * API 접근 권한 체크 등 보안 설정을 한다.
 */
@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    // BCryptPasswordEncoder를 사용하여 비밀번호 인코딩
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 보안 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeHttpRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정하겠다.
                // 회원가입 인증 제외
                .antMatchers(HttpMethod.POST, "/user").permitAll()
                // 테스트 영상 출력 페이지 인증 제외
                .antMatchers("/show").permitAll()
                // 공지사항 api 인증 제외
                .antMatchers("/noti").permitAll()
                // 카메라 아이디 확인 api 인증 제외
                .antMatchers("/checkCameraId").permitAll()
                // 로그인 인증 제외
                .antMatchers("/auth/login").permitAll()
                // fcm 테스트 api 인증 제외
                .antMatchers("/fcm/*").permitAll()
                // 영상 받는 websocket 주소 인증 제외
                .antMatchers("/video").permitAll()
                // 포지션 받는 websocket 주소 인증 제외
                .antMatchers("/position").permitAll()
                // 비상 대처 api 인증 제외
                .antMatchers("/emergency/*").permitAll()
                // 나머지는 모두 인증 필요
                .anyRequest().authenticated()

                .and()
                // 토큰 인증
                .addFilterBefore(new JwtFilter(tokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling()
                .accessDeniedHandler(new JwtAccessDeniedHandler())
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint());

        return httpSecurity.build();
    }

}