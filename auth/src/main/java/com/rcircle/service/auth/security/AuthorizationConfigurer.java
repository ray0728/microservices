package com.rcircle.service.auth.security;

import com.rcircle.service.auth.security.endpoint.LocalRedirectResolver;
import com.rcircle.service.auth.service.AccountService;
import com.rcircle.service.auth.service.GatewayService;
import com.rcircle.service.auth.utils.CompatRedisTokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import java.util.Arrays;

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
    @Resource
    private GatewayService gatewayService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;

    @Autowired
    private TokenEnhancer jwtTokenEnhancer;

    @Bean
    public TokenStore tokenStore() {
        return new CompatRedisTokenStore(connectionFactory);
    }


    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(clientConfigId)
                .secret(passwordEncoder.encode(ClientConfigSecret))
                .authorizedGrantTypes(clientConfigGrantTypes)
                .scopes(clientConfigScopes)
                .accessTokenValiditySeconds(upload_access_validity_second)
                .refreshTokenValiditySeconds(upload_refresh_validity_second)
                .autoApprove(true)
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
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(jwtTokenEnhancer, jwtAccessTokenConverter));
        endpoints
                .userDetailsService(mAccountService)
                .accessTokenConverter(jwtAccessTokenConverter)
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .redirectResolver(new LocalRedirectResolver().setGatewayService(gatewayService))
        ;
    }
}
