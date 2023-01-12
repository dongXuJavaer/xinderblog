package com.xinder.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.endpoint.TokenKeyEndpoint;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;

/**
 * @author Xinder
 * @date 2023-01-11 17:01
 */
@Configuration
@EnableAuthorizationServer
@Primary
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // 注入密码解析器， 就是BCryptPasswordEncoder
    @Autowired
    private PasswordEncoder passwordEncoder;

    // 注入自定义认证对象
    @Autowired
    @Qualifier(value = "userDetailServiceImpl")
    private UserDetailsService userDetailsService;

    // 注入认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private DataSource dataSource;


    // 引用证书读取工具类，这个里面的值在yml文件中注入了
    @Resource(name = "KeyProp")
    private KeyProperties keyProperties;

    /*
        Spring Security OAuth2会公开了两个端点，用于检查令牌（/oauth/check_token和/oauth/token_key），
        这些端点默认受保护denyAll()。tokenKeyAccess（）和checkTokenAccess（）方法会打开这些端点以供使用。
    */
    // 授权服务器端点访问权限验证方式
    @Override
    @Transactional(readOnly = true)
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients() // 允许 访问服务器端点需要进行客户端身份验证
                .passwordEncoder(passwordEncoder) // 设置客户端密码加密机制
                .tokenKeyAccess("permitAll()") // 开启token检查功能，permitAll()表示允许所有访问
                .checkTokenAccess("permitAll()"); // 开启token校验功能，permitAll()表示允许所有访问
    }

    // 客户端认证账号配置
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 从数据库中加载
        clients.jdbc(dataSource) // 一站式设置，在JdbcClientDetailsService设置了很多
                .passwordEncoder(passwordEncoder);
    }

    // 端点令牌存储方式、关联自定义认证对象、认证管理器
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints.tokenStore(new JwtTokenStore(this.jwtAccessTokenConverter()))  //令牌存储格式jwt
                .accessTokenConverter(this.jwtAccessTokenConverter()) //使用jwt令牌转换器处理请求令牌
                .authenticationManager(authenticationManager) // 关联认证管理器
                .userDetailsService(userDetailsService) // 关联自定义认证对象
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST); // 允许端点的访问方法
    }

    private JwtAccessTokenConverter jwtAccessTokenConverter() {
        // 在 JWT 编码的令牌值和 OAuth 身份验证信息（双向）之间转换的助手
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // 关联私钥
        jwtAccessTokenConverter.setKeyPair(this.keyPair());
        return jwtAccessTokenConverter;
    }

    // 读取密钥对的方法
    private KeyPair keyPair() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(
                keyProperties.getKeyStore().getLocation(),
                keyProperties.getKeyStore().getSecret().toCharArray()
        );
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(keyProperties.getKeyStore().getAlias());
        return keyPair;
    }

    // 秘钥证书读取
    @Bean("KeyProp")
    public KeyProperties keyProperties() {
        return new KeyProperties();
    }

    // 获取公钥的端点访问
    @Bean
    public TokenKeyEndpoint tokenKeyEndpoint() {
        return new TokenKeyEndpoint(this.jwtAccessTokenConverter());
    }
}
