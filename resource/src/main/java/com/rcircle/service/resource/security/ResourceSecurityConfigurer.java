package com.rcircle.service.resource.security;

import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
public class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/cate/list", "/tag/list").permitAll()
                .antMatchers("/blog/list", "/blog/files", "/blog/top").permitAll()
                .antMatchers("/blog/article").permitAll()
                .antMatchers("/blog/img/**", "/blog/video/**","/blog/cover/**").permitAll()
                .antMatchers("/reply/list", "/reply/new").permitAll()
                .antMatchers("/ref/**").permitAll()
                .anyRequest()
                .authenticated();
    }
}
