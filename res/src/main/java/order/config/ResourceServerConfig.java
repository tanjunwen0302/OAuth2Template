package order.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * @author Administrator
 * @version 1.0
 **/

/**
 * @EnableResourceServer 注解到一个 @Configuration 配置类上
 * 并且必须使用 ResourceServerConfigurer 这个配置对象来进行配置
 * （可以选择继承自 ResourceServerConfigurerAdapter 然后覆写其中的方法，
 * 参数就是这个 对象的实例）
 * <p>
 * ResourceServerSecurityConfigurer中主要包括：
 * --tokenServices：ResourceServerTokenServices 类的实例，用来实现令牌服务。
 * --tokenStore：TokenStore类的实例，指定令牌如何访问，与tokenServices配置可选
 * --resourceId：这个资源服务的ID，这个属性是可选的，但是推荐设置并在授权服务中进行验证。
 * 其他的拓展属性例如 tokenExtractor 令牌提取器用来提取请求中的令牌。
 * <p>
 * HttpSecurity配置这个与Spring Security类似：
 * 请求匹配器，用来设置需要进行保护的资源路径，默认的情况下是保护资源服务的全部路径。
 * 通过http.authorizeRequests()来设置受保护资源的访问规则
 * 其他的自定义权限保护规则通过 HttpSecurity 来进行配置。
 * @EnableResourceServer 注解自动增加了一个类型为
 * OAuth2AuthenticationProcessingFilter 的过滤器链
 */
@Configuration
@EnableResourceServer//资源服务
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {//继承资源服务
    @Autowired
    TokenStore tokenStore;

    //资源id，对应授权服务器中ClientDetailsServiceConfigurer配置的resourceIds
    @Value("${security.resourceId}")
    public String RESOURCE_ID;

    //验证令牌
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        //资源id
        resources.resourceId(RESOURCE_ID)
                .tokenStore(tokenStore)
                //验证令牌的服务
//                .tokenServices(tokenServices())
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //访问所有的uri需要有范围all
                .antMatchers("/**").access("#oauth2.hasScope('ROLE_ADMIN')")
                .and()
                .csrf().disable()
                //不创建session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }


    /**
     * ResourceServerTokenServices 是组成授权服务的另一半
     * 如果你的授权服务和资源服务在同一个应用程序上的话
     * 你可以使用 DefaultTokenServices
     * 这样的话，你就不用考虑关于实现所有必要的接口的一致性问题。
     * 如果你的资源服务器是分离开的
     * 那么你就必须要确保能够有匹配授权服务提供的 ResourceServerTokenServices
     * 它知道如何对令牌进行解码。
     * <p>
     * <p>
     * 令牌解析方法：
     * 使用 DefaultTokenServices 在资源服务器本地配置令牌存储、解码、解析方式
     * 使用 RemoteTokenServices 资源服务器通过 HTTP 请求来解码令牌，
     * 每次都请求授权服务器端点 /oauth/check_token
     * 使用授权服务的 /oauth/check_token 端点你需要在授权服务将这个端点暴露出去
     * 以便资源服务可以进行访问
     */
    //资源令牌解析服务
//    @Bean
//    public RemoteTokenServices tokenServices() {
//        //使用远程服务请求授权的服务器校验token,必须指定token的url,client_id,client_secret
//        RemoteTokenServices services = new RemoteTokenServices();
//        services.setCheckTokenEndpointUrl("http://127.0.0.1:53020/uaa/oauth/check_token");
//        services.setClientId("c1");
//        services.setClientSecret("secret");
//        return services;
//    }
}
