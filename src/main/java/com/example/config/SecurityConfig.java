//package com.example.config;
//
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationEventPublisher;
//import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.crypto.factory.PasswordEncoderFactories;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//
//@EnableWebSecurity //ウェブセキュリティの構成を有効にするためのアノテーション
//@Configuration //クラスが設定クラスであることを示すアノテーション
//@EnableMethodSecurity //クラスが設定クラスであることを示すアノテーション
//public class SecurityConfig {
//
//	/**
//	 * ユーザーの承認イベントを処理するメソッド
//	 * @param applicationEventPublisher
//	 * @return
//	 */
//	@Bean
//	protected AuthenticationEventPublisher authenticationEventPublisher
//	(ApplicationEventPublisher applicationEventPublisher) {
//		//DefaultAuthenticationEventPublisherを使用してデフォルトの認証イベントを発行します。
//		return new DefaultAuthenticationEventPublisher(applicationEventPublisher);
//	}
//	/**
//	 * ハッシュ化するためのpasswordエンコーダーメソッド 他のクラス（repositoryなど)で参照メソッドとして使用する
//	 * 
//	 * @return ハッシュ化された文字列（passwordで使用)
//	 */
//	@Bean
//	protected PasswordEncoder passwordEncoder() {
//		@SuppressWarnings("unused")
//		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//		//BCryptPasswordEncoderを使用してパスワードをハッシュ化
//		//ハッシュ化されたパスワードは、16回のハッシュ化処理を行う
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(16);
//		return encoder;
//	}
//}
