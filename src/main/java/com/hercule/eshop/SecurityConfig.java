package com.hercule.eshop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Bean
	private BCryptPasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	protected void configure(AuthenticationManagerBuilder auth) throws Exception
	{
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}
	
	protected void configure(HttpSecurity http) throws Exception
	{
	    http.authorizeRequests().anyRequest().hasAnyRole("ADMIN", "USER")
	    .and()
	    .authorizeRequests().antMatchers("/login**").permitAll()
	    .and()
	    .formLogin().loginPage("/login").loginProcessingUrl("/loginAction").permitAll()
	    .and()
	    .logout().logoutSuccessUrl("/login").permitAll()
	    .and()
	    .csrf().disable();
	}
}