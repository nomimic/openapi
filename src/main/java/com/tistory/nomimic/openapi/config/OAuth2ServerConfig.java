package com.tistory.nomimic.openapi.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStoreUserApprovalHandler;

/**
 * Created by Lucas,Lee on 14. 11. 14..
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2ServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //클라이언트의 기본 정보를 메모리상에 등록한다.
        //지속적으로 클라이언트를 관리할려면, DB 또는 기타 스토리지에 정보를 저장하여 사용해야 한다.
        clients.inMemory()
                .withClient("oauth2-client")
                .resourceIds("OPEN_API") //자원 아이디 지정
                .authorizedGrantTypes("client_credentials")
                .authorities("ROLE_CLIENT")
                .secret("password")
                .scopes("encoder");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager);
        endpoints.userApprovalHandler(new ApprovalStoreUserApprovalHandler());
    }
}
