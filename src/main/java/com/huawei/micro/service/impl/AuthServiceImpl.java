package com.huawei.micro.service.impl;

import com.huawei.micro.common.ResultCode;
import com.huawei.micro.entity.User;
import com.huawei.micro.exception.BusinessException;
import com.huawei.micro.mapper.UserMapper;
import com.huawei.micro.service.AuthService;
import com.huawei.micro.util.Md5Util;
import com.huawei.micro.util.TokenStore;
import com.huawei.micro.vo.LoginResponseVO;
import com.huawei.micro.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现类
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final int USER_STATUS_ENABLED = 1;

    private final UserMapper userMapper;
    private final TokenStore tokenStore;

    @Override
    public LoginResponseVO login(LoginVO loginVO) {
        User user = userMapper.selectUserByUsername(loginVO.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }

        String encryptedPassword = Md5Util.encrypt(loginVO.getPassword());
        if (!encryptedPassword.equals(user.getPassword())) {
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "用户名或密码错误");
        }

        if (user.getStatus() == null || user.getStatus() != USER_STATUS_ENABLED) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "用户已被禁用");
        }

        String token = tokenStore.generateToken(user.getId());

        LoginResponseVO responseVO = new LoginResponseVO();
        responseVO.setToken(token);
        responseVO.setUserId(user.getId());
        responseVO.setUsername(user.getUsername());
        return responseVO;
    }
}
