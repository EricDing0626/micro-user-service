package com.huawei.micro.util;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token 内存存储组件。
 *
 * @author Eric
 * @since 1.0.0
 */
@Component
public class TokenStore {

    private final Map<String, Long> tokenUserMap = new ConcurrentHashMap<>();

    /**
     * 生成并存储 token。
     *
     * @param userId 用户 ID
     * @return token
     */
    public String generateToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        tokenUserMap.put(token, userId);
        return token;
    }

    /**
     * 根据 token 获取用户 ID。
     *
     * @param token 登录 token
     * @return 用户 ID，无效 token 返回 null
     */
    public Long getUserId(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        return tokenUserMap.get(token);
    }

    /**
     * 移除 token。
     *
     * @param token 登录 token
     */
    public void removeToken(String token) {
        if (token != null) {
            tokenUserMap.remove(token);
        }
    }
}
