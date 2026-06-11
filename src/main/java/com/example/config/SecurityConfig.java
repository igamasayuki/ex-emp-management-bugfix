package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * セキュリティ設定のためのクラス.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * パスワードをハッシュ化（BCrypt）してSpringに登録する.
     */
    @Bean //@Autowiredさせるために必要。外部のライブラリを管理したいときに使用。
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 現在の画面遷移を維持するための設定.
     * <p>
     * これがないと自動的に全てのURLにアクセス制限がかかってしまう。
     */
    @Bean
    //フレームワーク起動時に自動的に呼び出される
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        authorize -> authorize.anyRequest().permitAll() //全てのURLのアクセスを許可する
                )
                .csrf(csrf -> csrf.disable()); //CSRFを無効にする

        return http.build();
    }
}