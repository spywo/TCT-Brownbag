package com.autodesk.icp.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.autodesk.icp.community.authentication.CustomAuthenticationFilter;
import com.autodesk.icp.community.authentication.CustomAuthenticationProvider;
import com.autodesk.icp.community.authentication.CustomAuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
//@Import(WebSocketSecurityConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/fonts/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/home", "/login").permitAll().anyRequest().authenticated();
        
        http.authenticationProvider(customAuthenticationProvider());
        

        http.addFilterBefore(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider();
    }
    
    @Bean
    public CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationManager(super.authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(new CustomAuthenticationSuccessHandler());
        
        return filter;
    }
}