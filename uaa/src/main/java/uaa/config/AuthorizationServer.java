package uaa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Arrays;

/**
 * @author tanjunwen
 * @version 1.0
 * @Purpose OAuth2认证授权配置
 * @Date
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    //从数据库中读取的编码器
    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private ClientDetailsService clientDetailsService;

    @Resource
    private AuthorizationCodeServices authorizationCodeServices;

    //认证管理器
    @Autowired
    private AuthenticationManager authenticationManager;

    //jwt加强
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;

    @Autowired
    PasswordEncoder passwordEncoder;

    //设置授权码模式授权码如何存取，暂时采用内存方式
//    @Bean
//    public AuthorizationCodeServices authorizationCodeServices() {
//        return new InMemoryAuthorizationCodeServices();
//    }

    @Bean
    public AuthorizationCodeServices authorizationCodeServices(DataSource dataSource) {
        return new JdbcAuthorizationCodeServices(dataSource);//设置授权码模式的授权码如何存取
    }


    /**
     * ClientDetailsServiceConfigurer 能够使用内存或者JDBC来实现客户端详情服务（ClientDetailsService），
     * ClientDetailsService负责查找ClientDetails，而ClientDetails有几个重要的属性如下列表：
     * -clients: (必须的)用来标识客户的id
     * -secret: (需要值得信任的客户端) 客户端安全码，如果有的话
     * -scope: 用来限制客户端的访问范围，如果为空（默认）的话，则允许访问所有内容
     * -authorizedGrantTypes: 此客户端可以使用授权类型，默认为空
     * -authorities: 此客户端可以使用的权限（基于springSecurity authorities）
     * <p>
     * 客户端详情（Client Details）能够在应用程序运行的时候进行更新
     * 可以通过访问底层的存储服务（例如将客户 端详情存储在一个关系数据库的表中,就可以使用 JdbcClientDetailsService）
     * 或者通过自己实现 ClientRegistrationService接口（同时你也可以实现 ClientDetailsService 接口）来进行管理
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //暂时使用内存方式存储用户客户端详情
//        clients.inMemory()//in-memory存储
//                .withClient("c1")//client_id客户端id
//                .secret(new BCryptPasswordEncoder().encode("secret"))//客户端密钥
//                .resourceIds("res1")//可访问的资源id
//                .authorizedGrantTypes("authorization_code",
//                        "password", "client_credentials", "implicit", "refresh_token")//该客户端允许的授权的类型
//                .scopes("all")//允许的范围
//                .autoApprove(false)//false 跳转到授权页面,ture则直接发令牌
//                //加上验证回调地址
//                .redirectUris("https://www.bing.com");
        //从数据库中读取客户端配置
        clients.withClientDetails(clientDetailsService);
    }

    @Bean
    public ClientDetailsService clientDetailsService(DataSource dataSource) {
        ClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        ((JdbcClientDetailsService) clientDetailsService).setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }


    /**
     * 管理令牌
     * 配置令牌
     */
    @Bean
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices services = new DefaultTokenServices();
        //客户端数据服务
        services.setClientDetailsService(clientDetailsService);
        //支持刷新令牌
        services.setSupportRefreshToken(true);
        //设置令牌存储策略
        services.setTokenStore(tokenStore);

        //令牌增强，使用jwt
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        services.setTokenEnhancer(tokenEnhancerChain);

        //设置令牌时间24小时
        services.setAccessTokenValiditySeconds(7200);
        //刷新令牌3天
        services.setRefreshTokenValiditySeconds(259200);

        return services;
    }

    /**
     * 令牌访问端点配置
     * AuthorizationServerEndpointsConfigurer 这个对象的实例可以完成令牌服务以及令牌endpoint配置
     * <p>
     * AuthorizationServerEndpointsConfigurer 通过设定以下属性决定支持的授权类型（Grant Types）:
     * <p>
     * --authenticationManager：认证管理器，当你选择了资源所有者密码（password）授权类型的时候
     * 请设置 这个属性注入一个 AuthenticationManager 对象。
     * <p>
     * --userDetailsService：如果你设置了这个属性的话，那说明你有一个自己的 UserDetailsService 接口的实现，
     * 或者你可以把这个东西设置到全局域上面去（例如 GlobalAuthenticationManagerConfigurer 这个配置对 象），
     * 当你设置了这个之后，那么 "refresh_token" 即刷新令牌授权类型模式的流程中就会包含一个检查，用来确保这个账号是否仍然有效，
     * 假如说你禁用了这个账户的话。
     * <p>
     * --authorizationCodeServices：这个属性是用来设置授权码服务的（即 AuthorizationCodeServices 的实例对 象），
     * 主要用于 "authorization_code" 授权码类型模式。
     * <p>
     * --implicitGrantService：这个属性用于设置隐式授权模式，用来管理隐式授权模式的状态。
     * <p>
     * --tokenGranter：当你设置了这个东西（即 TokenGranter 接口实现），那么授权将会交由你来完全掌控，并且会忽略掉上面的这几个属性，
     * 这个属性一般是用作拓展用途的，即标准的四种授权模式已经满足不了你的 需求的时候，才会考虑使用这个。
     * <p>
     * <p>
     * 《《配置授权端点的URL（Endpoint URLs）：》》
     * AuthorizationServerEndpointsConfigurer 这个配置对象有一个叫做 pathMapping() 的方法用来配置端点URL链接，它有两个参数：
     * 第一个参数：String 类型的，这个端点URL的默认链接。
     * 第二个参数：String 类型的，你要进行替代的URL链接。
     * <p>
     * 以上的参数都将以 "/" 字符为开始的字符串，框架的默认URL链接如下列表，可以作为这个 pathMapping() 方法的 第一个参数：
     * /oauth/authorize：授权端点。
     * /oauth/token：令牌端点。
     * /oauth/confirm_access：用户确认授权提交端点。
     * /oauth/error：授权服务错误信息端点。
     * /oauth/check_token：用于资源服务访问的令牌解析端点。
     * /oauth/token_key：提供公有密匙的端点，如果你使用JWT令牌的话。
     * <p>
     * 需要注意的是授权端点这个URL应该被Spring Security保护起来只供授权用户访问.
     */


    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)//密码模式需要
                .authorizationCodeServices(authorizationCodeServices)//授权码模式需要
                //令牌管理服务，都需要
                .tokenServices(tokenServices())
                //允许的令牌端点请求方法，在此只允许post方法
                .allowedTokenEndpointRequestMethods(HttpMethod.POST);
    }

    /**
     * AuthorizationServerSecurityConfigurer：用来配置令牌端点(Token Endpoint)的安全约束
     * 令牌访问的安全策略
     */

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //tokenkey这个endpoint当使用JwtToken且使用非对称加密时，资源服务用于获取公钥而开放的，这里指这个 endpoint完全公开
        security.tokenKeyAccess("permitAll()")//oauth/token_key公开
                // checkToken这个endpoint完全公开
                .checkTokenAccess("permitAll()") //oauth/check_token公开
                //允许表单认证（申请令牌）
                .allowFormAuthenticationForClients();
    }
}
/**
 * 授权服务配置总结：授权服务配置分成三大块，可以关联记忆。
 * <p>
 * -既然要完成认证，它首先得知道客户端信息从哪儿读取，因此要进行客户端详情配置。
 * -既然要颁发token，那必须得定义token的相关endpoint，以及token如何存取，以及客户端支持哪些类型的token。
 * -既然暴露除了一些endpoint，那对这些endpoint可以定义一些安全上的约束等
 */
