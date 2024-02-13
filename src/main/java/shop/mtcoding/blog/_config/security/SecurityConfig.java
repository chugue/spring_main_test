package shop.mtcoding.blog._config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain configure (HttpSecurity http) throws Exception{
        //  HTTP 요청에 대한 인증 및 권한 부여 정책을 설정
        //  "/user/updateForm" 경로와 "/board/"로 시작하는 모든 경로에 대해 인증된 사용자만 접근을 허용
        http.authorizeHttpRequests( a -> {
            a.requestMatchers("/user/updateForm", "/board/**").authenticated()
                    .anyRequest().permitAll(); //위에서 명시하지 않은 모든 다른 요청에 대해서는
        });                                    //모든 사용자(인증 여부와 상관없이)에게 접근을 허용
        // 폼 기반 로그인을 구성
        //  사용자 정의 로그인 페이지 경로를 설정 -> "/loginForm"
        http.formLogin(f -> {
            f.loginPage("/loginForm");
        });
        // 설정된 HttpSecurity 객체를 기반으로 SecurityFilterChain을 빌드하고 반환
        return http.build();
    }
}
