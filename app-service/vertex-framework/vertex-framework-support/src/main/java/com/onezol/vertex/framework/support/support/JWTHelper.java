package com.onezol.vertex.framework.support.support;

import com.onezol.vertex.framework.common.util.SpringUtils;
import com.onezol.vertex.framework.common.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * JWT 工具类
 */
public final class JWTHelper {

    private JWTHelper() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 默认getSecret()
     */
    public static final String DEFAULT_SECRET_KEY = "";
    /**
     * 默认过期时间(秒)
     */
    public static final Integer DEFAULT_EXPIRATION_TIME = 3600;

    /**
     * 生成 JWT
     *
     * @param subject JWT 主题
     * @return JWT
     */
    public static String generateToken(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + getExpireTime() * 1000);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, getSecret())
                .compact();
    }

    /**
     * 验证 JWT
     *
     * @param token JWT
     * @return 是否有效
     */
    public static boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // JWT 验证失败
            return false;
        }
    }

    /**
     * 从 JWT 中获取 subject
     *
     * @param token JWT
     * @return subject
     */
    public static String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
            return claims.getSubject();
        } catch (Exception e) {
            // JWT 解析失败或无效
            return null;
        }
    }

    /**
     * 从 JWT 中获取 claims
     *
     * @param token JWT
     * @return claims
     */
    public static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser().setSigningKey(getSecret()).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            // JWT 解析失败或无效
            return null;
        }
    }

    public static String getSecret() {
        String secretKey = SpringUtils.getRequiredProperty("application.jwt.secret-key");
        if (StringUtils.isEmpty(secretKey)) {
            return DEFAULT_SECRET_KEY;
        }
        return secretKey;
    }

    public static Integer getExpireTime() {
        String expirationTime = SpringUtils.getRequiredProperty("application.jwt.expiration-time");
        if (StringUtils.isEmpty(expirationTime)) {
            return DEFAULT_EXPIRATION_TIME;
        }
        return Integer.parseInt(expirationTime);
    }

}
