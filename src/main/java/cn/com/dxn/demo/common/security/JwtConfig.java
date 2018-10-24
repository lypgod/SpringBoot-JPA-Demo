package cn.com.dxn.demo.common.security;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author richard
 */
@Data
@Component
public class JwtConfig {
    @Value("${security.jwt.uri:/auth/**}")
    private String uri;

    @Value("${security.jwt.uri:/auth/login}")
    private String loginUrl;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private int expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;
}