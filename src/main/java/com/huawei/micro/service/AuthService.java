package com.huawei.micro.service;

import com.huawei.micro.vo.LoginResponseVO;
import com.huawei.micro.vo.LoginVO;

/**
 * 认证业务服务接口。
 *
 * @author Eric
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 用户登录。
     *
     * @param loginVO 登录参数
     * @return 登录结果（含 token）
     */
    LoginResponseVO login(LoginVO loginVO);
}
