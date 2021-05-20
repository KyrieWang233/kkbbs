package com.kyriewang.kkbbs.shiro;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;


//@ConfigurationProperties(prefix = "kyriewang.jwt")
@PropertySource(value = { "classpath:user.properties" }, encoding = "utf-8")
@Slf4j
@Component
public class JwtUtils {
    @Value("${kyriewang.jwt.secret}")
    private String secret;
    @Value("${kyriewang.jwt.expire}")
    private String expire;
    @Value("${kyriewang.jwt.header}")
    private String header;
    /**
     * 生成jwt token
     */
    public String generateToken(long userId) {
        Date nowDate = new Date();
        //过期时间

        long e = Long.parseLong(expire);
        Date expireDate = new Date(nowDate.getTime() + e * 1000);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject(userId+"")
                .setIssuedAt(nowDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // 获取jwt的信息
    public Claims getClaimByToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        }catch (Exception e){
            log.debug("validate is token error ", e);
            return null;
        }
    }

    /**
     * token是否过期
     * @return  true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }
}