package com.huawei.micro.exception;

import com.huawei.micro.common.ResultCode;
import lombok.Getter;

/**
 * 自定义业务异常。
 *
 * @author Eric
 * @since 1.0.0
 */
@Getter
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 业务状态码。
     */
    private final int code;

    /**
     * 构造业务异常（默认 500）。
     *
     * @param message 错误信息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.FAIL.getCode();
    }

    /**
     * 构造业务异常。
     *
     * @param code    状态码
     * @param message 错误信息
     */
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * 构造业务异常。
     *
     * @param resultCode 状态码枚举
     */
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 构造业务异常。
     *
     * @param resultCode 状态码枚举
     * @param message    自定义错误信息
     */
    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }
}
