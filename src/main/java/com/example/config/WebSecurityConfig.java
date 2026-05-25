package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Securityの設定クラス（5-3, 5-4）.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                .requestMatchers("/", "/insert", "/toInsert", "/login").permitAll()
                // 講師自用の演習パス（本番完成形とは別系統）
                .requestMatchers("/junior/**", "/middle/**", "/middleEmployee/**", "/advance/**", "/advanceEmployee/**").permitAll()
                .requestMatchers("/employee/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/")
                .loginProcessingUrl("/login")
                .usernameParameter("mailAddress")
                .passwordParameter("password")
                .successHandler(loginSuccessHandler)
                .failureUrl("/?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .permitAll()
            )
            .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
