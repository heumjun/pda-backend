package com.denso.pdabackend.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.denso.pdabackend.filter.JwtFilter;
import com.denso.pdabackend.response.exception.CustomAccessDeniedHandler;
import com.denso.pdabackend.response.exception.CustomAuthenticationEntryPoint;
import com.denso.pdabackend.token.util.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity      //EnableWebSecurity  어노테이션이 붙는것만으로 springSecurityFilterChain이 자동으로 설정됨. (로그인창이 뜸)
@RequiredArgsConstructor
public class SecurityConfiguration{

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    
    //토큰 예외처리할 경로들
    private static final String[] WHITE_LIST = {
        "/swagger-resources/**",
        "/swagger-ui.html",
        "/webjars/**",
        "/swagger/**",
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/exception/**",
        "/login"
        
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        
        http
            .httpBasic(c->c.disable())  // security에서 기본으로 생성하는 login페이지 사용 안 함 
            .csrf(c->c.disable())       //rest-api 방식이기때문에 disable =>  csrf(모놀리식 구조에 적합함)
            //.cors(c->c.disable())     //다른도메인에서 호출할경우 권한부여(openfeign에서 호출할경우 java단에서 호출되기때문에 cors영향을 받지 않는다. js단에서 호출할경우 cors정책막힘.)
            .authorizeHttpRequests(auth->auth.anyRequest().authenticated())  //인증이 되야만 접근가능
            .exceptionHandling(exceptionHandling->{
                exceptionHandling
                    .authenticationEntryPoint(customAuthenticationEntryPoint)   //토큰인증이 되지않은경우
                    .accessDeniedHandler(customAccessDeniedHandler);            //허가되지 않는 링크일경우(role 권한)
            })
            .sessionManagement(sessionManagement->sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) //JWT 사용으로 session 사용안함.
            .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);   // ID, Password 검사 전에 jwt 필터 먼저 수행
            
        return http.build();
        
        
    }

    /**
     * 토큰 예외처리
     * @return
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)->web.ignoring().requestMatchers(WHITE_LIST);
    }

}
