package com.rcircle.service.auth.security;

import com.rcircle.service.auth.utils.SimpleDate;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.HashMap;
import java.util.Map;

public class JWTTokenEnhancer implements TokenEnhancer {
    private static final String TAG_AUTHINFO = "AUTHINFO";
    private static final String TAG_TIMESTAMP = "TIMESTAMP";

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        Map<String, Object> additionalInfo = new HashMap<>();
        additionalInfo.put(TAG_AUTHINFO, "Published by SERVICE-AUTH");
        additionalInfo.put(TAG_TIMESTAMP, SimpleDate.getUTCString());
        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);
        return accessToken;
    }
}
