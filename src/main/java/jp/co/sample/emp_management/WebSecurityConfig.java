package jp.co.sample.emp_management;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import jp.co.sample.emp_management.service.AdministratorDetailsServiceImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private AdministratorDetailsServiceImpl administratorDetailsServiceImpl;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/css/**", "/img/**", "/js/**", "/fonts/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/toInsert", "/insert").permitAll().anyRequest()
				.authenticated();
		http.formLogin().loginProcessingUrl("/login").loginPage("/")
				.usernameParameter("mailAddress").passwordParameter("password")
				.defaultSuccessUrl("/employee/showList", true).failureUrl("/?error=true");
		http.logout().logoutUrl("/logout").logoutSuccessUrl("/").permitAll()
				.deleteCookies("JSESSIONID").invalidateHttpSession(true);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(administratorDetailsServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
