package com.rcircle.service.gateway.security;

import com.rcircle.service.gateway.model.Authority;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableOAuth2Sso
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception{
        http.authorizeRequests()
                .antMatchers("/debug/**").permitAll()
                .antMatchers("/","/login**").permitAll()
                .antMatchers("/admin/**").hasRole(Authority.ROLE_ADMIN)
                .antMatchers("/test/**").hasRole(Authority.ROLE_SUPER)
                .anyRequest().authenticated();
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/global/**");
    }
}
