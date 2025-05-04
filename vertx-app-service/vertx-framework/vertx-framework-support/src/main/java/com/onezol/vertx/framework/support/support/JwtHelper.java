package com.onezol.vertx.framework.support.support;

import com.onezol.vertx.framework.common.util.SpringUtils;
import com.onezol.vertx.framework.common.util.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * JWT 工具类，提供生成、验证 JWT 以及从 JWT 中提取信息的方法。
 */
public final class JwtHelper {

    /**
     * 默认密钥，当配置文件中未指定密钥时使用。
     */
    public static final String DEFAULT_SECRET_KEY = "";

    /**
     * 默认过期时间，单位为秒，当配置文件中未指定过期时间时使用。
     */
    public static final Integer DEFAULT_EXPIRATION_TIME = 3600;

    private JwtHelper() {
        throw new IllegalStateException("Utility class cannot be instantiated");
    }

    /**
     * 生成 JWT 令牌，使用 HS256 算法进行签名。
     *
     * @param subject JWT 的主题，通常用于存储用户标识等信息。
     * @return 生成的 JWT 令牌字符串。
     */
    public static String createJwt(String subject) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + getExpireTime() * 1000L);
        SecretKey secretKey = getSigningKey();

        return Jwts.builder()
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 验证 JWT 令牌的有效性，包括签名验证和过期时间检查。
     *
     * @param jwt 待验证的 JWT 令牌字符串。
     * @return 如果令牌有效则返回 {@code true}，否则返回 {@code false}。
     */
    public static boolean isJwtValid(String jwt) {
        try {
            parseJwt(jwt);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 从 JWT 令牌中提取主题信息。
     *
     * @param jwt 待提取信息的 JWT 令牌字符串。
     * @return 提取的主题信息，如果解析失败则返回 {@code null}。
     */
    public static String extractSubjectFromJwt(String jwt) {
        return getClaimFromJwt(jwt, Claims::getSubject);
    }

    /**
     * 从 JWT 令牌中提取所有的声明信息。
     *
     * @param jwt 待提取信息的 JWT 令牌字符串。
     * @return 提取的声明信息对象，如果解析失败则返回 {@code null}。
     */
    public static Claims extractClaimsFromJwt(String jwt) {
        return parseJwt(jwt).getPayload();
    }

    /**
     * 内部辅助方法，用于从 JWT 令牌中提取指定的声明信息。
     *
     * @param <T>            声明信息的类型。
     * @param jwt            待提取信息的 JWT 令牌字符串。
     * @param claimsResolver 用于解析声明信息的函数。
     * @return 提取的声明信息，如果解析失败则返回 {@code null}。
     */
    private static <T> T getClaimFromJwt(String jwt, Function<Claims, T> claimsResolver) {
        Claims claims = parseJwt(jwt).getPayload();
        return claimsResolver.apply(claims);
    }

    /**
     * 内部辅助方法，用于解析 JWT 令牌并返回签名的声明信息。
     *
     * @param jwt 待解析的 JWT 令牌字符串。
     * @return 解析后的签名声明信息对象。
     * @throws JwtException 如果解析过程中出现异常。
     */
    private static Jws<Claims> parseJwt(String jwt) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(jwt);
    }

    /**
     * 内部辅助方法，用于获取签名密钥。
     * 从配置文件中获取密钥，如果未配置则使用默认密钥。
     *
     * @return 用于签名的密钥对象。
     */
    private static SecretKey getSigningKey() {
        String secret = getSecret();
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 从配置文件中获取 JWT 密钥。
     * 如果配置文件中未指定密钥，则返回默认密钥。
     *
     * @return JWT 密钥字符串。
     */
    public static String getSecret() {
        String secretKey = SpringUtils.getRequiredProperty("application.jwt.secret-key");
        return StringUtils.isEmpty(secretKey) ? DEFAULT_SECRET_KEY : secretKey;
    }

    /**
     * 从配置文件中获取 JWT 令牌的过期时间。
     * 如果配置文件中未指定过期时间，则返回默认过期时间。
     *
     * @return JWT 令牌的过期时间，单位为秒。
     */
    public static Integer getExpireTime() {
        String expirationTime = SpringUtils.getRequiredProperty("application.jwt.expiration-time");
        return StringUtils.isEmpty(expirationTime) ? DEFAULT_EXPIRATION_TIME : Integer.parseInt(expirationTime);
    }
}
