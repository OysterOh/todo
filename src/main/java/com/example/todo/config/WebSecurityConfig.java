package com.example.todo.config;

import com.example.todo.filter.JwtAuthFilter;
import com.example.todo.userapi.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

//@Configuration  // 설정 클래스 용도로 사용하도록 스프링에 등록하는 어노테이션
@EnableWebSecurity  // 시큐리티 설정 파일로 사용할 클래스 선언
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
//자동 권한검사를 수행하기 위한 설정
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 시큐리티 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //Security 모듈이 기본적으로 제공하는 보안 정책 해제
        http
                .cors()
                //Cross Origin Resource Sharing: 다른 도메인에서 리소스 요청을 허용하는 정책을 설정하는데 사용
                .and()

                .csrf().disable()
                //Cross Site Request Forgery
                //  : 웹 애플리케이션의 특정한 요청에 대한 인증을 위한 토큰을 생성하고 검증하는 보안기능

                .httpBasic().disable()
                //HTTP 기본 인증 비활성화
                //(기본 인증: 사용자의 아이디와 패스워드를 요청 헤더에 포함시켜 인증을 수행하는 방식)

                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                //세션 인증을 사용하지 않겠다
                .and()
                //어떤 요청에서 인증을 안할 것인지 설정, 언제 할 것인지 설정
                .authorizeRequests()
                .antMatchers(HttpMethod.PUT, "/api/auth/promote")
                .authenticated()
                .antMatchers("/api/auth/load-profile").authenticated()
                .antMatchers("/", "/api/auth/**")
                .permitAll()
//                .antMatchers(HttpMethod.POST, "/api/todos").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;

        // 토큰 인정 필터 연결
        http.addFilterAfter(
                jwtAuthFilter,
                CorsFilter.class    //import spring
        );


        return http.build();
    }
}
