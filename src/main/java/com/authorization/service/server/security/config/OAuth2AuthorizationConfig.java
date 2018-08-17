package com.authorization.service.server.security.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration
@EnableAuthorizationServer
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${authorization-server.certificate.key}")
    private String secretKey;

    @Value("${authorization-server.accessTokenValiditySeconds}")
    private Integer accessTokenValiditySeconds;

    @Value("${authorization-server.refreshTokenValiditySeconds}")
    private Integer refreshTokenValiditySeconds;

    private final AuthenticationManager authenticationManager;

    @Autowired
    public OAuth2AuthorizationConfig(@Qualifier("authenticationManagerBean") AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory()

                .withClient("browser")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("ui")
//                .autoApprove(true)
//                .authorities("ACTUATOR")
                .accessTokenValiditySeconds(accessTokenValiditySeconds) //Access token is only valid for ? minutes.
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds) //Refresh token is only valid for ? minutes.
//				.resourceIds(authServerResourceId/*, "oauth2-resource"*/) //определяет к каким ресурсам может обращаться

                .and()
                .withClient("auth_server")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("auth_server")
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)


                .and()
                .withClient("adminka")
                .authorizedGrantTypes("refresh_token", "password", "client_credentials")
                .scopes("server", "ui")
                .accessTokenValiditySeconds(accessTokenValiditySeconds)
                .refreshTokenValiditySeconds(refreshTokenValiditySeconds)

                .and()
                .withClient("auth")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                .scopes("server")
                .authorities("ROLE_ADMIN")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(600)

                .and()
                .withClient("customer-service")
//					.secret(env.getProperty("DEMO_SERVICE_PASSWORD"))
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "password")
                .scopes("server")
//                .resourceIds(authServerResourceId, "oauth2-resource")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)

                .and()
                .withClient("listing-service")
//					.secret(env.getProperty("DEMO_SERVICE_PASSWORD"))
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
//                .resourceIds(authServerResourceId, "oauth2-resource")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)

                .and()
                .withClient("catalog-service")
//					.secret(env.getProperty("DEMO_SERVICE_PASSWORD"))
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
//                .resourceIds(authServerResourceId, "oauth2-resource")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)


                //dev services ====================
                .and()
                .withClient("mail-service")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token")
                .scopes("server")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(6000)

                .and()
                .withClient("ask-service")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "authorization_code")
                .scopes("server", "ui")
                .authorities("ROLE_ADMIN")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(600)

                .and()
                .withClient("demo-service")
                .secret("secret")
                .authorizedGrantTypes("client_credentials", "refresh_token", "authorization_code")
                .scopes("server")
                .accessTokenValiditySeconds(300)
                .refreshTokenValiditySeconds(600);
                //========================================


    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints
                .authenticationManager(authenticationManager)
                .tokenServices(tokenServices())
                .tokenStore(tokenStore())
                .accessTokenConverter(jwtAccessTokenConverter());
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) {
        oauthServer
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        KeyPair keyPair = new KeyStoreKeyFactory(
                new ClassPathResource("jwt.jks"), secretKey.toCharArray())
                .getKeyPair("jwt");
        converter.setKeyPair(keyPair);
        return converter;
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setTokenStore(tokenStore());
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setTokenEnhancer(jwtAccessTokenConverter());
        return defaultTokenServices;
    }
}
