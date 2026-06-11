package com.huawei.micro.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 密码加密工具类。
 *
 * @author Eric
 * @since 1.0.0
 */
public final class Md5Util {

    private Md5Util() {
    }

    /**
     * 对明文进行 MD5 加密。
     *
     * @param plainText 明文
     * @return 密文，入参为 null 时返回 null
     */
    public static String encrypt(String plainText) {
        if (plainText == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(plainText.getBytes(StandardCharsets.UTF_8));
            StringBuilder stringBuilder = new StringBuilder();
            for (byte digestByte : digest) {
                stringBuilder.append(String.format("%02x", digestByte));
            }
            return stringBuilder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 algorithm not available", e);
        }
    }
}
