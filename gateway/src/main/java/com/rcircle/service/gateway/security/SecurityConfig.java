package com.rcircle.service.gateway.security;

import com.rcircle.service.gateway.model.Authority;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/debug/**").permitAll()
                .antMatchers("/", "/join**", "/rst/redirect").permitAll()
                .antMatchers("/admin/**").hasRole(Authority.ROLE_ADMIN)
                .anyRequest().authenticated()
        .and()
        .httpBasic().disable()
        .formLogin().loginPage("/login").permitAll()
        ;
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/global/**");
    }
}
