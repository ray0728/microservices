package com.rcircle.service.gateway.security;

import com.rcircle.service.gateway.filters.OAuth2SsoAuthenticationProcessingFilter;
import com.rcircle.service.gateway.model.Authority;
import com.rcircle.service.gateway.security.authentication.OAuthFeignAuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Bean
    public OAuthFeignAuthenticationProvider oAuthFeignAuthenticationProvider() {
        return new OAuthFeignAuthenticationProvider();
    }

    @Bean
    public OAuth2SsoAuthenticationProcessingFilter oAuth2SsoAuthenticationProcessingFilter() {
        OAuth2SsoAuthenticationProcessingFilter filter = new OAuth2SsoAuthenticationProcessingFilter();
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(oAuthFeignAuthenticationProvider());
    }

    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/debug/**").permitAll()
                .antMatchers("/home", "/", "/join**", "/rst/redirect").permitAll()
                .antMatchers("/admin/**").hasRole(Authority.ROLE_ADMIN)
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .formLogin().loginPage("/login").permitAll()
                .and()
                .logout().permitAll()
        ;
        http.addFilterBefore(oAuth2SsoAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/global/**");
    }
}
