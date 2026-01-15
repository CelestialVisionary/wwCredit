package com.wwfinance.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * JWT令牌工具类 - 兼容Java 21和Spring Boot 3
 */
public final class TokenUtil {

    // 加解密密匙（至少32字节）
    private static final String SECRET = "vsofo-5grcs-secret-key-1234567890-abcdefghijk"; 
    // 过期时间/秒（7天）
    private static final long EXPIRATION = 604800L;
    // JWT令牌前缀
    private static final String TOKEN_PREFIX = "5grcs ";

    // 私有构造函数，防止实例化
    private TokenUtil() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * 生成JWT令牌
     * @param claims 令牌声明
     * @return JWT令牌
     */
    private static String generateToken(Map<String, Object> claims) {
        // 使用Java 21特性：var关键字
        var expirationInstant = Instant.now().plus(EXPIRATION, ChronoUnit.SECONDS);
        return Jwts.builder()
                .claims(claims)
                .expiration(java.util.Date.from(expirationInstant))
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                .compact();
    }

    /**
     * 从令牌中获取声明
     * @param token JWT令牌
     * @return 令牌声明
     */
    private static Optional<Claims> getClaimsFromToken(String token) {
        try {
            // 使用JJWT 0.12.5版本API，适配Spring Boot 3.x和Java 21
            var claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.of(claims);
        } catch (Exception e) {
            // 改进异常处理，返回Optional.empty()而非null
            return Optional.empty();
        }
    }

    /**
     * 检查令牌是否失效
     * @param token JWT令牌
     * @return true 失效 false 未失效
     */
    public static boolean isTokenExpired(String token) {
        // 检查令牌格式
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return true;
        }
        
        // 提取纯令牌
        var pureToken = token.substring(TOKEN_PREFIX.length());
        
        // 使用Java 21特性：Optional
        return getClaimsFromToken(pureToken)
                .map(claims -> claims.getExpiration().before(new java.util.Date()))
                .orElse(true);
    }
    
    /**
     * 获取令牌的过期时间
     * @param token JWT令牌
     * @return 过期时间的时间戳（秒），如果令牌无效则返回0
     */
    public static long getTokenExpiration(String token) {
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return 0;
        }
        
        var pureToken = token.substring(TOKEN_PREFIX.length());
        
        return getClaimsFromToken(pureToken)
                .map(claims -> claims.getExpiration().getTime() / 1000L)
                .orElse(0L);
    }

    /**
     * 生成用户JWT令牌
     * @param phone 用户手机号
     * @param userId 用户ID
     * @return JWT令牌
     */
    public static String generateUserToken(String phone, Long userId) {
        // 使用Java 21特性：var关键字
        var claims = new HashMap<String, Object>();
        claims.put("token_phone", phone);
        claims.put("token_user_id", userId);
        return TOKEN_PREFIX + generateToken(claims);
    }

    /**
     * 从令牌中获取用户信息
     * @param token JWT令牌
     * @return 用户信息映射
     */
    public static Map<String, String> getUserInfoFromToken(String token) {
        var userInfo = new HashMap<String, String>();
        
        if (token == null || !token.startsWith(TOKEN_PREFIX)) {
            return userInfo;
        }
        
        var pureToken = token.substring(TOKEN_PREFIX.length());
        
        getClaimsFromToken(pureToken).ifPresent(claims -> {
            // 使用Java 21特性：Optional.ifPresent
            userInfo.put("token_phone", claims.get("token_phone").toString());
            if (claims.get("token_user_id") != null) {
                userInfo.put("token_user_id", claims.get("token_user_id").toString());
            }
        });
        
        return userInfo;
    }

    /**
     * 获取令牌前缀
     * @return 令牌前缀
     */
    public static String getTokenPrefix() {
        return TOKEN_PREFIX;
    }

    public static void main(String[] args) {
        // 测试生成令牌
        var token = generateUserToken("18538313406", 1L);
        System.out.println("生成的令牌：" + token);
        
        // 测试解析令牌
        var userInfo = getUserInfoFromToken(token);
        System.out.println("解析的用户信息：" + userInfo);
        
        // 测试令牌是否过期
        var isExpired = isTokenExpired(token);
        System.out.println("令牌是否过期：" + isExpired);
        
        // 测试获取过期时间
        var expirationTime = getTokenExpiration(token);
        System.out.println("令牌过期时间：" + expirationTime);
    }
}