package com.ray.service.account.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.service.account.model.Authority;
import com.ray.service.account.util.JwtAccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ResourceSecurityConfigurer extends ResourceServerConfigurerAdapter {
    @Value("${security.oauth2.jwt.publickey.local}")
    private String local_public_key;
    @Value("${security.oauth2.jwt.publickey.remote}")
    private String remote_public_key;

    private String getPubKey() {
        Resource resource = new ClassPathResource(local_public_key);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return br.lines().collect(Collectors.joining("\n"));
        } catch (IOException ioe) {
            return getKeyFromAuthorizationServer();
        }
    }

    private String getKeyFromAuthorizationServer() {
        ObjectMapper objectMapper = new ObjectMapper();
        String pubKey = new RestTemplate().getForObject(remote_public_key, String.class);
        try {
            Map map = objectMapper.readValue(pubKey, Map.class);
            return map.get("value").toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessToken();
        converter.setVerifierKey(getPubKey());
        return converter;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config.tokenServices(tokenServices());
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }


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
