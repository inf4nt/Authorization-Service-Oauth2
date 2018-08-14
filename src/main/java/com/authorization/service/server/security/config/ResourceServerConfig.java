package com.authorization.service.server.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableResourceServer
@EnableOAuth2Client
@Order(99)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    private final DefaultTokenServices tokenServices;

    private final TokenStore tokenStore;

    @Autowired
    public ResourceServerConfig(DefaultTokenServices tokenServices, TokenStore tokenStore) {
        this.tokenServices = tokenServices;
        this.tokenStore = tokenStore;
    }

    /**
     * Method configures {@link HttpSecurity} to secure resources
     *
     * For explanation between configuration {@link HttpSecurity} in {@link WebSecurityConfigurerAdapter}
     * and {@link ResourceServerConfigurerAdapter} follow:
     * https://stackoverflow.com/questions/44234159/using-websecurityconfigureradapter-with-spring-oauth2-and-user-info-uri
     * https://stackoverflow.com/questions/38034217/spring-boot-and-oauth2-websecurityconfigureradapter-vs-resourceserverconfigurer
     * https://stackoverflow.com/questions/37288715/spring-oauth2-httpsecurity-http-resourceserverconfigurer-and-websecurityconf?rq=1
     * https://stackoverflow.com/questions/45980267/resourceserverconfigureradapter-vs-websecurityconfigureradapter
     * https://github.com/spring-projects/spring-security-oauth/issues/1024
     *
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(STATELESS);
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer config) {
        config
                .tokenServices(tokenServices)
                .tokenStore(tokenStore);
    }


}
