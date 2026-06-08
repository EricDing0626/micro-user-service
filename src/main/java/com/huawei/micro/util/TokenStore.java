package com.huawei.micro.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 简单 Token 存储（内存）
 */
@Component
public class TokenStore {

    private final Map<String, Long> tokenUserMap = new ConcurrentHashMap<>();

    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenUserMap.put(token, userId);
        return token;
    }

    public Long getUserId(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        return tokenUserMap.get(token);
    }

    public void removeToken(String token) {
        if (token != null) {
            tokenUserMap.remove(token);
        }
    }
}
