package com.example.config;

import com.example.filter.LoginCheckFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Spring Securityの設定.
 */
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, LoginCheckFilter loginCheckFilter) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                // 認可判定は学習用に自作Filterへ寄せ、Spring SecurityのFilterChain上で直URLを止める。
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
                .addFilterBefore(loginCheckFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
