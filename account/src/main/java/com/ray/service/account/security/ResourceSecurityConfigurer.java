package com.ray.service.account.security;

import com.ray.service.account.model.Authority;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;


@Configuration
public class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/account/info")
                .permitAll()
                .antMatchers(HttpMethod.PUT, "/account/create")
                .permitAll()
                .antMatchers(HttpMethod.DELETE, "/account/delete")
                .hasAnyRole(Authority.ROLE_ADMIN, Authority.ROLE_DEBUG)
                .antMatchers(HttpMethod.PUT, "/account/change")
                .hasAnyRole(Authority.ROLE_ADMIN, Authority.ROLE_DEBUG)
                .anyRequest()
                .authenticated();
    }
}
