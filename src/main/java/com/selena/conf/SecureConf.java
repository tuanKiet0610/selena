package com.selena.conf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.selena.service.UserDetailsServiceImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.beans.Customizer;


@Configuration
@EnableWebSecurity
public class SecureConf extends WebSecurityConfigurerAdapter{
	@Autowired
	private JwtFilter jwtFilter;
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
				.authorizeRequests()
				.antMatchers(
						"/css/**","/js/**","/upload/**","/img/**",
						"/login","/logout",
						"/oauth2/**",              // khởi tạo OAuth2
						"/login/oauth2/**"         // callback từ IdP
				).permitAll()
				.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
				.anyRequest().authenticated()
				.and()
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.formLogin()
				.loginPage("/login")
				.loginProcessingUrl("/login_processing_url")
				.successHandler(new UrlAuthenticationSuccessHandler())
				.failureUrl("/login?login_error=true")
				.and()
				.oauth2Login()
				.loginPage("/login")
				.defaultSuccessUrl("/home", true)   // dùng URL cố định để tránh lỗi handler
				.and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/home")
				.invalidateHttpSession(true).deleteCookies("JSESSIONID")
				.and()
				.rememberMe().key("uniqueAndSecret").tokenValiditySeconds(86400);
	}


	@Autowired
	private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder(4));
	}
	
	public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder(4).encode("123456"));
		System.out.println(new BCryptPasswordEncoder(4).encode("guest"));
	}
	
}
