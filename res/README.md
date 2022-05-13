# 资源服务

----
该服务为通过网关得到认证服务器颁发令牌后调用的服务

----
tokenConfig配置

```
@Configuration
public class TokenConfig {
    //从application.yml文件中读取的对称加密密钥
    @Value("${security.symmetricKey}")
    private String SIGNING_KEY;

    @Bean
    public TokenStore tokenStore() {
        //JWT令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(SIGNING_KEY); //对称加密的秘钥，资源服务器使用该秘钥来验证
        return converter;
    }

}
```

ResourceServerConfig 资源配置
```
@Configuration
@EnableResourceServer//资源服务
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {//继承资源服务
    @Autowired
    TokenStore tokenStore;

    //资源id，对应授权服务器中ClientDetailsServiceConfigurer配置的resourceIds
    //从applicaton.yml中读取，资源id
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

}

```


- 端口为5031
