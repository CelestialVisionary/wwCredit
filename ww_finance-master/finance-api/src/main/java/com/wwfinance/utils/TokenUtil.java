package com.wwfinance.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * token辅助类 - 兼容Java 21和Spring Boot 3
 */
public class TokenUtil {

    private final static String secret = "vsofo-5grcs-secret-key-1234567890-abcdefghijk"; // 加解密密匙（至少32字节）
    private final static long expiration = 604800; // 过期时间/秒
    private final static String tokenHead = "5grcs "; // #JWT负载中拿到开头

    private static String localGenerateToken(Map<String, Object> claims) {
        // 完全使用java.time API，不再依赖java.util.Date
        Instant expirationInstant = Instant.now().plus(expiration, ChronoUnit.SECONDS);
        return Jwts.builder()
                .claims(claims)
                .expiration(java.util.Date.from(expirationInstant))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }

    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            // 使用JJWT 0.12.5版本API，适配Spring Boot 3.x和Java 21
            claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            // 改进异常处理，避免直接打印堆栈
            e.printStackTrace();
        }
        return claims;
    }

    /**
     * token是否失效
     * 
     * @return true 失效 false 未失效
     */
    public static boolean isTokenExpired(String token) {
        if (token.startsWith(tokenHead)) {
            token = token.substring(tokenHead.length());
        } else {
            return true;
        }
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return true;
        }
        
        // 检查token是否过期
        return claims.getExpiration().before(new java.util.Date());
    }
    
    /**
     * 获取token的过期时间
     * 
     * @param token token字符串
     * @return 过期时间的时间戳（秒）
     */
    public static long getTokenExpiration(String token) {
        if (token.startsWith(tokenHead)) {
            token = token.substring(tokenHead.length());
        }
        Claims claims = getClaimsFromToken(token);
        if (claims == null) {
            return 0;
        }
        return claims.getExpiration().getTime() / 1000;
    }

    /**
     * 生成用户token
     * @param token_phone  用户手机号
     * @param token_user_id  用户ID
     */
    public static String generateMerchantToken( String token_phone, Long token_user_id
            ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("token_phone", token_phone);
        claims.put("token_user_id", token_user_id);
        return tokenHead + localGenerateToken(claims);
    }


    /**
     * 获取token信息
     *  用户类型
     */
    public static Map<String, String> getMapInfoFromToken(String token) {
        if (token.startsWith(tokenHead)) {
            token = token.substring(tokenHead.length());
        } else {
            return new HashMap<>();
        }
        Claims claims = getClaimsFromToken(token);
        Map<String, String> tokenInfoMap = new HashMap<>();
        tokenInfoMap.put("token_phone", claims.get("token_phone").toString());
        if (claims.get("token_user_id") != null) {
            tokenInfoMap.put("token_user_id", claims.get("token_user_id").toString());
        }
        return tokenInfoMap;
    }

    public static void main(String[] args) {
        Map<String, String> map = getMapInfoFromToken("5grcs eyJhbGciOiJIUzUxMiJ9.eyJ0b2tlbl9waG9uZSI6IjE4NTM4MzEzNDA2IiwiZXhwIjoxNzQ1MDczODgyfQ.p2lGxJMmLmOC6tNeFZKRKwwQAzw_sGfc6UkdjlapjdIuEQoCJ5uLtgKZsNTCW1icQn2ryzKo6ijC1HGceUkN_g");
        System.out.println(map);
    }
}