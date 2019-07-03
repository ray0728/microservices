package com.rcircle.service.gateway.security.authentication;

import com.rcircle.service.gateway.clients.RemoteRequestWithTokenInterceptor;
import com.rcircle.service.gateway.model.Account;
import com.rcircle.service.gateway.services.AccountService;
import com.rcircle.service.gateway.services.OAuth2SsoService;
import com.rcircle.service.gateway.utils.HttpContextHolder;
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

    @Resource
    private AccountService accountService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String token = oAuth2SsoService.getAccessToken(authentication.getName(), (String) authentication.getCredentials());
        if(token.startsWith("failed!")){
           throw new BadCredentialsException(token);
        }
        ((Account)authentication).setToken(token);
        HttpContextHolder.getContext().setValue(
                RemoteRequestWithTokenInterceptor.ACCESSTOKEN,
                ((Account) authentication).getToken().getAccess_token());
        ((Account)authentication).copyFrom(accountService.afterLoginSuccess());
        authentication.setAuthenticated(true);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }
}
