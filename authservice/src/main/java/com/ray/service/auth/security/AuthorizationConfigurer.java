package com.ray.service.auth.security;

import com.ray.service.auth.service.AccountService;
import com.ray.service.auth.util.CompatRedisTokenStore;
import com.ray.service.auth.util.JwtAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;

@Configuration
@EnableCaching
@EnableAuthorizationServer
public class AuthorizationConfigurer extends AuthorizationServerConfigurerAdapter {
    @Value("${token.upload.access.validity}")
    private int upload_access_validity_second;

    @Value("${token.upload.refresh.validity}")
    private int upload_refresh_validity_second;

    @Value("${token.browse.access.validity}")
    private int browse_access_validity_second;

    @Value("${token.browse.refresh.validity}")
    private int browse_refresh_validity_second;

    @Value("${tokenkey.jks.private.key}")
    private String tokenkey_jks_private_key;

    @Value("${client.config.id}")
    private String clientConfigId;

    @Value("${client.config.secret}")
    private String ClientConfigSecret;

    @Value("#{'${client.config.grant}'.split(';')}")
    private String[] clientConfigGrantTypes;

    @Value("#{'${client.config.scopes}'.split(';')}")
    private String[] clientConfigScopes;

    @Resource
    private AccountService mAccountService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public TokenStore tokenStore() {
        return new CompatRedisTokenStore(connectionFactory);
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        final JwtAccessTokenConverter converter = new JwtAccessToken();
        KeyStoreKeyFactory keyStoreKeyFactory =
                new KeyStoreKeyFactory(new ClassPathResource("tokenkey.jks"), tokenkey_jks_private_key.toCharArray());
        converter.setKeyPair(keyStoreKeyFactory.getKeyPair("tokenkey"));
        return converter;
    }


    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientConfigId)
                .secret(passwordEncoder.encode(ClientConfigSecret))
                .authorizedGrantTypes(clientConfigGrantTypes)
                .scopes(clientConfigScopes)
                .accessTokenValiditySeconds(upload_access_validity_second)
                .refreshTokenValiditySeconds(upload_refresh_validity_second)
        ;
    }

    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
//                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .userDetailsService(mAccountService)
                .accessTokenConverter(accessTokenConverter())
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
        ;
    }
}
