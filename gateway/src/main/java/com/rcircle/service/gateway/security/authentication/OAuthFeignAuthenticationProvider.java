package com.rcircle.service.gateway.security.authentication;

import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.services.OAuth2SsoService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class OAuthFeignAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private OAuth2SsoService oAuth2SsoService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = oAuth2SsoService.getAccessToken(authentication.getName(), authentication.getCredentials().toString());
        if(token.startsWith("failed!")){
           throw new BadCredentialsException("Incorrect Password!");
        }
        ((Account)authentication).setAccesstoken(token);
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }
}
