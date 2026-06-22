package com.neuedu.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.neuedu.entity.Admin;
import com.neuedu.entity.User;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final String KEY = "hyx";
    // 设置令牌过期时间为30分钟
    private static final long EXPIRATION_TIME = 30 * 60 * 1000; // 30分钟

    public static String createToken(User user) {
        return JWT.create()
                .withClaim("id", user.getId())
                .withClaim("phone", user.getPhone())
                .withClaim("email", user.getEmail())
                .withClaim("role", "user")
                .withClaim("date", new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间
                .sign(Algorithm.HMAC256(KEY));
    }

    public static String createToken(Admin admin) {
        return JWT.create()
                .withClaim("id", admin.getId())
                .withClaim("phone", admin.getPhone())
                .withClaim("email", admin.getEmail())
                .withClaim("role", "admin")
                .withClaim("date", new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 设置过期时间
                .sign(Algorithm.HMAC256(KEY));
    }

    public static User check1(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
        Long id = verify.getClaim("id").asLong();
        String phone = verify.getClaim("phone").asString();
        String email = verify.getClaim("email").asString();
        User user = new User();
        user.setId(id);
        user.setPhone(phone);
        user.setEmail(email);
        return user;
    }

    public static Admin check(String token) {
        DecodedJWT verify = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
        Long id = verify.getClaim("id").asLong();
        String phone = verify.getClaim("phone").asString();
        String email = verify.getClaim("email").asString();
        Admin admin = new Admin();
        admin.setId(id);
        admin.setPhone(phone);
        admin.setEmail(email);
        return admin;
    }

    public static Map<String, Object> parseRoleAndId(String token) {
        try {
            DecodedJWT verify = JWT.require(Algorithm.HMAC256(KEY)).build().verify(token);
            Map<String, Object> result = new HashMap<>();
            result.put("id", verify.getClaim("id").asLong());
            result.put("role", verify.getClaim("role").asString());
            return result;
        } catch (Exception e) {
            throw new RuntimeException("Token 解析失败", e);
        }
    }
}
