package com.xinder.user.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
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
import javax.annotation.Resources;
import javax.sql.DataSource;
import java.security.KeyPair;

/**
 * @author Xinder
 * @date 2023-01-11 17:01
 */
@Configuration
@EnableAuthorizationServer
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Primary
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    // 注入密码解析器， 就是BCryptPasswordEncoder

    @Resource(name = "myPasswordEncoder")
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

    @Value("${auth.clientId}")
    private String clientId;

    @Value("${auth.clientSecret}")
    private String clientSecret;


    // 引用证书读取工具类，这个里面的值在yml文件中注入了
    @Resource(name = "KeyProp")
    private KeyProperties keyProperties;

    /*
        Spring Security OAuth2会公开了两个端点，用于检查令牌（/oauth/check_token和/oauth/token_key），
        这些端点默认受保护denyAll()。tokenKeyAccess（）和checkTokenAccess（）方法会打开这些端点以供使用。
    */
    //

    /**
     * 用来配置令牌端点的安全约束.
     * <p>
     * 授权服务器端点访问权限验证方式
     *
     * @param security
     * @throws Exception
     */
    @Override
    @Transactional(readOnly = true)
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients() // 允许 访问服务器端点需要进行客户端身份验证
                .passwordEncoder(passwordEncoder) // 设置客户端密码加密机制
                .tokenKeyAccess("permitAll()") // 开启token检查功能，permitAll()表示允许所有访问
                .checkTokenAccess("permitAll()"); // 开启token校验功能，permitAll()表示允许所有访问
    }

    //

    /**
     * 客户端认证账号配置
     * <p>
     * 用来配置客户端详情服务（ClientDetailsService），
     * 客户端详情信息在这里进行初始化，你能够把客户端详情信息写死在这里或者是通过数据库来存储调取详情信息。
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        // 从数据库中加载
//        clients.jdbc(dataSource) // 一站式设置，在JdbcClientDetailsService设置了很多
//                .passwordEncoder(passwordEncoder);
        // 在内存中创建认证账号
        clients.inMemory()
                .withClient(clientId) // 账号名称，客户端client_id
                .secret(passwordEncoder.encode(clientSecret)) // 密码，需要设置加密
//                .secret("$2a$10$0fcFfsFd9AehjOJmwYRnu.GgvuvHgoXg6wgqbiFCVTPYcA4pAKQti") // 密码，需要设置加密
                .resourceIds("oauth2-resource", "user", "dongyimai-goods") // 资源编号， oauth2-resource需要填写默认
                .scopes("server", "app", "web", "wx") // 作用范围
                .authorizedGrantTypes("authorization_code", "password", "refresh_token",
                        "client_credentials", "implicit")  // 授权登录模式 :授权码认证、密码认证、刷新token 、客户端认证、隐式认证、
                .redirectUris("http://localhost");  //登录成功跳转地址
    }

    /**
     * 端点令牌存储方式、关联自定义认证对象、认证管理器
     * <p>
     * 用来配置令牌（token）的访问端点和令牌服务(token services)。
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {

        endpoints
                .tokenStore(new JwtTokenStore(this.jwtAccessTokenConverter()))  //令牌存储格式jwt
                .accessTokenConverter(this.jwtAccessTokenConverter()) //使用jwt令牌转换器处理请求令牌
                .authenticationManager(authenticationManager) // 关联认证管理器
                .userDetailsService(userDetailsService) // 关联自定义认证对象
//                .authorizationCodeServices()  // 这个属性是用来设置授权码服务的（即 AuthorizationCodeServices的实例对象），主要用于 “authorization_code” 授权码类型模式。
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
