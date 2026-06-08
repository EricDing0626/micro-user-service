package com.huawei.micro.service;

import com.huawei.micro.vo.LoginResponseVO;
import com.huawei.micro.vo.LoginVO;

/**
 * 认证服务接口
 */
public interface AuthService {

    LoginResponseVO login(LoginVO loginVO);
}
