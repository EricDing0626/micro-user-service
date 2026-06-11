package com.huawei.micro.controller;

import com.huawei.micro.common.Result;
import com.huawei.micro.common.ResultCode;
import com.huawei.micro.service.UserService;
import com.huawei.micro.vo.PageResultVO;
import com.huawei.micro.vo.UserBatchDeleteResultVO;
import com.huawei.micro.vo.UserBatchDeleteVO;
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
 * 用户管理 REST 接口。
 *
 * @author Eric
 * @since 1.0.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    /**
     * 新增用户。
     *
     * @param createVO 用户新增参数
     * @return 新用户 ID
     */
    @PostMapping
    public Result<Long> createUser(@Valid @RequestBody UserCreateVO createVO) {
        Long userId = userService.createUser(createVO);
        return Result.success("新增用户成功", userId);
    }

    /**
     * 分页查询用户列表。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param username 用户名（模糊匹配）
     * @return 分页用户列表
     */
    @GetMapping
    public Result<PageResultVO<UserDetailVO>> listUsers(
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize,
            @RequestParam(required = false) String username) {
        return Result.success(userService.listUsers(pageNum, pageSize, username));
    }

    /**
     * 根据 ID 查询用户详情。
     *
     * @param id 用户 ID
     * @return 用户详情
     */
    @GetMapping("/{id}")
    public Result<UserDetailVO> getUserById(@PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    /**
     * 修改用户信息。
     *
     * @param updateVO 用户修改参数
     * @return 操作结果
     */
    @PutMapping
    public Result<Void> updateUser(@Valid @RequestBody UserUpdateVO updateVO) {
        userService.updateUser(updateVO);
        return Result.success();
    }

    /**
     * 批量删除用户。
     *
     * @param batchDeleteVO 批量删除参数
     * @return 批量删除结果
     */
    @DeleteMapping("/batch")
    public Result<UserBatchDeleteResultVO> batchDeleteUsers(@Valid @RequestBody UserBatchDeleteVO batchDeleteVO) {
        UserBatchDeleteResultVO result = userService.batchDeleteUsers(batchDeleteVO.getIds());
        if (result.getSuccessCount() == 0) {
            return Result.fail(ResultCode.NOT_FOUND.getCode(), "批量删除失败，用户均不存在", result);
        }
        if (result.getFailedCount() > 0) {
            return Result.success("部分用户删除成功", result);
        }
        return Result.success("批量删除成功", result);
    }

    /**
     * 根据 ID 删除用户。
     *
     * @param id 用户 ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteUserById(@PathVariable Long id) {
        userService.deleteUserById(id);
        return Result.success();
    }
}
