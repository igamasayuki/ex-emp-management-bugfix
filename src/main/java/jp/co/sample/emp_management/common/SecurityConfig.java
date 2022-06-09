package jp.co.sample.emp_management.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//	存在しないサービス
	@Autowired
	private UserDetailsService memberDetailsService;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/img/**", "/js/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// リクエストを許可する範囲を設定
		http.authorizeRequests()
				.antMatchers("/", "/toInsert", "/insert")
				.permitAll()
				.anyRequest()
				.authenticated();

		http.formLogin()
				.loginPage("/")
				.loginProcessingUrl("/login")
				.failureUrl("/")
				.defaultSuccessUrl("/employee/showList", true)
				.usernameParameter("mailAddress")
				.passwordParameter("password")
				.permitAll();
		
		http.logout()
				.logoutUrl("/logout")
				.logoutSuccessUrl("/")
				.permitAll();
//				.invalidateHttpSession(true);
//				.logoutRequestMatcher(new AntPathRequestMatcher("/logout**"))
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
