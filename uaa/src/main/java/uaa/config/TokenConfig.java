package uaa.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * @author tanjunwen
 * @version 1.0
 * @Purpose token配置
 * @Date 2022/5/12
 * */
@Configuration
public class TokenConfig {
    /**
     * 从配置文件中读取对称加密密钥
     * */
    @Value("${security.symmetricKey}")
    private String SINGING_KEY;

    /**
     * 对称加密，在本地直接校验jwt
     * */
    @Bean
    public JwtAccessTokenConverter accessTokenConverter(){
        JwtAccessTokenConverter converter=new JwtAccessTokenConverter();
        //对称密钥，资源服务器使用此秘钥来验证
        converter.setSigningKey(SINGING_KEY);
        return converter;
    }

    /**
     * 使用jwt令牌
     * */
    @Bean
    public TokenStore tokenStore(){
        //jwt令牌存储方案
        return new JwtTokenStore(accessTokenConverter());
    }

}
