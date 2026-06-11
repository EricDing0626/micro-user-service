package com.huawei.micro.controller;

import com.huawei.micro.common.Result;
import com.huawei.micro.service.AuthService;
import com.huawei.micro.vo.LoginResponseVO;
import com.huawei.micro.vo.LoginVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 认证 REST 接口。
 *
 * @author Eric
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    /**
     * 用户登录。
     *
     * @param loginVO 登录参数
     * @return 登录结果（含 token）
     */
    @PostMapping("/login")
    public Result<LoginResponseVO> login(@Valid @RequestBody LoginVO loginVO) {
        LoginResponseVO responseVO = authService.login(loginVO);
        return Result.success("登录成功", responseVO);
    }
}
