package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Spring Securityの設定クラス.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    /**
     * パスワードハッシュ化用のエンコーダーをBean登録します.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * セキュリティフィルタチェーンの設定.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                // 静的リソースは全員許可
                .requestMatchers("/css/**", "/img/**", "/js/**").permitAll()
                // ログイン・登録関連のパスは全員許可
                .requestMatchers("/", "/insert", "/toInsert", "/login").permitAll()
                // 各演習用のパスは独立性を保つために一律開放（Controller内の自作ロジックで動作確認させる）
                .requestMatchers("/junior/**", "/middle/**", "/middleEmployee/**", "/advance/**", "/advanceEmployee/**").permitAll()
                // 本来の従業員管理ページは認証が必要
                .requestMatchers("/employee/**").authenticated()
                .anyRequest().authenticated()
            )
            .formLogin(login -> login
                .loginPage("/") 
                .loginProcessingUrl("/login")
                .usernameParameter("mailAddress")
                .passwordParameter("password")
                .defaultSuccessUrl("/employee/showList", true)
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
