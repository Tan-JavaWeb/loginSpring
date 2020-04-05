package com.example.loginspirng.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Trong lớp WebSecurityConfig, gọi interface UserDetailsService để cấu hình. Do
	// đó phải inject UserDetailsService.
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private DataSource dataSource;

	// Mã hóa mật khẩu
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) {
		try {
			// Đặt dịch vụ để tìm kiếm User trong Database.
			// Đặt PasswordEncoder.
			auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// Token stored in Table (Persistent_Logins)
	@Bean
	public PersistentTokenRepository persistentTokenRepository() {
		JdbcTokenRepositoryImpl db = new JdbcTokenRepositoryImpl();
		db.setDataSource(dataSource);
		return db;
	}

	// cấu hình các chi tiết về bảo mật
	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		// (1)Các trang không yêu cầu login
		http.authorizeRequests().antMatchers("/", "/login", "/logout").permitAll();

		// (2) Trang /userInfo yêu cầu phải login với vai trò ROLE_USER hoặc
		// ROLE_ADMIN.Nếu chưa login, nó sẽ redirect tới trang /login.
		http.authorizeRequests().antMatchers("/userInfo").access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')");

		// (3) Trang chỉ dành cho ADMIN
		http.authorizeRequests().antMatchers("/admin").access("hasRole('ROLE_ADMIN')");
		
		// (4) Khi người dùng đã login, với vai trò XX.
        // Nhưng truy cập vào trang yêu cầu vai trò YY,
        // Ngoại lệ AccessDeniedException sẽ ném ra.
		http.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
		
		 // Cấu hình cho Login Form.
		http.authorizeRequests().and().formLogin()
			.loginProcessingUrl("/j_spring_security_check")  // Submit URL của trang login
			.loginPage("/login")
			.defaultSuccessUrl("/userAccountInfo")
			.failureUrl("login?error=true")
			.usernameParameter("username")
			.passwordParameter("password")
			.and().logout().logoutUrl("/logout").logoutSuccessUrl("/logoutSuccessful");  // Cấu hình cho Logout Page.
			
		
		
		// Cấu hình Remember Me.
		http.authorizeRequests().and()
			.rememberMe().tokenRepository(this.persistentTokenRepository())
			.tokenValiditySeconds(1*24*60*60);  //1 ngày
	}
	
	
}
