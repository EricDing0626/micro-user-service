package com.huawei.micro.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一 API 返回结果封装。
 *
 * @param <T> 业务数据类型
 * @author Eric
 * @since 1.0.0
 */
@Data
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 状态码。
     */
    private int code;

    /**
     * 提示信息。
     */
    private String message;

    /**
     * 业务数据。
     */
    private T data;

    public Result() {
    }

    /**
     * 构造返回结果。
     *
     * @param code    状态码
     * @param message 提示信息
     * @param data    业务数据
     */
    public Result(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    /**
     * 返回成功结果（无数据）。
     *
     * @param <T> 业务数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success() {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 返回成功结果。
     *
     * @param data 业务数据
     * @param <T>  业务数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 返回成功结果。
     *
     * @param message 提示信息
     * @param data    业务数据
     * @param <T>     业务数据类型
     * @return 成功结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(ResultCode.SUCCESS.getCode(), message, data);
    }

    /**
     * 返回失败结果。
     *
     * @param message 错误信息
     * @param <T>     业务数据类型
     * @return 失败结果
     */
    public static <T> Result<T> fail(String message) {
        return new Result<>(ResultCode.FAIL.getCode(), message, null);
    }

    /**
     * 返回失败结果。
     *
     * @param code    状态码
     * @param message 错误信息
     * @param <T>     业务数据类型
     * @return 失败结果
     */
    public static <T> Result<T> fail(int code, String message) {
        return new Result<>(code, message, null);
    }

    /**
     * 返回失败结果。
     *
     * @param resultCode 状态码枚举
     * @param <T>        业务数据类型
     * @return 失败结果
     */
    public static <T> Result<T> fail(ResultCode resultCode) {
        return new Result<>(resultCode.getCode(), resultCode.getMessage(), null);
    }
}
