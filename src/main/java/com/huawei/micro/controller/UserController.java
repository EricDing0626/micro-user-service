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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
@Api(tags = "用户管理")
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
    @ApiOperation(value = "新增用户", notes = "创建用户并可选分配角色，返回新用户 ID")
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
    @ApiOperation(value = "分页查询用户列表", notes = "支持按用户名模糊搜索")
    @GetMapping
    public Result<PageResultVO<UserDetailVO>> listUsers(
            @ApiParam("页码，默认 1") @RequestParam(required = false) Integer pageNum,
            @ApiParam("每页条数，默认 10") @RequestParam(required = false) Integer pageSize,
            @ApiParam("用户名模糊搜索") @RequestParam(required = false) String username) {
        return Result.success(userService.listUsers(pageNum, pageSize, username));
    }

    /**
     * 根据 ID 查询用户详情。
     *
     * @param id 用户 ID
     * @return 用户详情
     */
    @ApiOperation(value = "查询用户详情", notes = "根据用户 ID 查询详情及角色列表")
    @GetMapping("/{id}")
    public Result<UserDetailVO> getUserById(@ApiParam("用户 ID") @PathVariable Long id) {
        return Result.success(userService.getUserById(id));
    }

    /**
     * 修改用户信息。
     *
     * @param updateVO 用户修改参数
     * @return 操作结果
     */
    @ApiOperation(value = "修改用户", notes = "支持修改基本信息、密码和角色")
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
    @ApiOperation(value = "批量删除用户", notes = "支持部分成功，返回成功和失败 ID 列表")
    @DeleteMapping("/batch")
    public Result<UserBatchDeleteResultVO> batchDeleteUsers(@Valid @RequestBody UserBatchDeleteVO batchDeleteVO) {
        return buildBatchDeleteResponse(userService.batchDeleteUsers(batchDeleteVO.getIds()));
    }

    /**
     * 根据 ID 删除用户。
     *
     * @param id 用户 ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除用户", notes = "逻辑删除用户并级联删除角色关联")
    @DeleteMapping("/{id}")
    public Result<Void> deleteUserById(@ApiParam("用户 ID") @PathVariable Long id) {
        userService.deleteUserById(id);
        return Result.success();
    }

    /**
     * 构建批量删除响应。
     *
     * @param result 批量删除结果
     * @return 统一响应
     */
    private Result<UserBatchDeleteResultVO> buildBatchDeleteResponse(UserBatchDeleteResultVO result) {
        if (result.getSuccessCount() == 0) {
            return Result.fail(ResultCode.NOT_FOUND.getCode(), "批量删除失败，用户均不存在", result);
        }
        if (result.getFailedCount() > 0) {
            return Result.success("部分用户删除成功", result);
        }
        return Result.success("批量删除成功", result);
    }
}
