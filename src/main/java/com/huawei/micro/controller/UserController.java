package com.huawei.micro.controller;

import com.huawei.micro.common.Result;
import com.huawei.micro.service.UserService;
import com.huawei.micro.vo.PageResultVO;
import com.huawei.micro.vo.UserCreateVO;
import com.huawei.micro.vo.UserDetailVO;
import com.huawei.micro.vo.UserUpdateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户管理 REST 接口
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping
    public Result<Long> createUser(@Valid @RequestBody UserCreateVO createVO) {
        Long userId = userService.createUser(createVO);
        return Result.success("新增用户成功", userId);
    }

    @GetMapping
    public Result<PageResultVO<UserDetailVO>> listUsers(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String username) {
        return Result.success(userService.listUsers(pageNum, pageSize, username));
    }

    @GetMapping("/{id}")
    public Result<UserDetailVO> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    @PutMapping
    public Result<Void> updateUser(@Valid @RequestBody UserUpdateVO updateVO) {
        userService.updateUser(updateVO);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return Result.success();
    }
}
