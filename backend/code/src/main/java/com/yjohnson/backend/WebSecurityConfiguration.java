package com.yjohnson.backend;

import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/*
https://newbedev.com/spring-boot-security-postman-gives-401-unauthorized
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter implements ApplicationContextAware {

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
		    .antMatchers("/").permitAll()
		    .antMatchers(HttpMethod.POST, "/users").permitAll()
		    .antMatchers(HttpMethod.GET, "/users/").permitAll()
		    .antMatchers(HttpMethod.DELETE, "/users").permitAll()
		    .antMatchers(HttpMethod.POST, "/users/login").permitAll()
		    .antMatchers(HttpMethod.GET, "/users/all").permitAll()
		    .antMatchers("/interests").permitAll()
		    .antMatchers("/interests/all").permitAll()
		    .anyRequest().authenticated();
	}
}