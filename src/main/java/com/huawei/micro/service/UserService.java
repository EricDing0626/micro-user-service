package com.huawei.micro.service;

import com.huawei.micro.vo.PageResultVO;
import com.huawei.micro.vo.UserBatchDeleteResultVO;
import com.huawei.micro.vo.UserCreateVO;
import com.huawei.micro.vo.UserDetailVO;
import com.huawei.micro.vo.UserUpdateVO;

import java.util.List;

/**
 * 用户业务服务接口。
 *
 * @author Eric
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 新增用户。
     *
     * @param createVO 用户新增参数
     * @return 新用户 ID
     */
    Long createUser(UserCreateVO createVO);

    /**
     * 根据 ID 查询用户详情。
     *
     * @param id 用户 ID
     * @return 用户详情
     */
    UserDetailVO getUserById(Long id);

    /**
     * 分页查询用户列表。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param username 用户名（模糊匹配，可为空）
     * @return 分页结果
     */
    PageResultVO<UserDetailVO> listUsers(Integer pageNum, Integer pageSize, String username);

    /**
     * 修改用户信息。
     *
     * @param updateVO 用户修改参数
     */
    void updateUser(UserUpdateVO updateVO);

    /**
     * 根据 ID 删除用户。
     *
     * @param id 用户 ID
     */
    void deleteUserById(Long id);

    /**
     * 批量删除用户。
     *
     * @param ids 用户 ID 列表
     * @return 批量删除结果
     */
    UserBatchDeleteResultVO batchDeleteUsers(List<Long> ids);
}
