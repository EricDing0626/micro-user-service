package com.huawei.micro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huawei.micro.entity.Role;
import com.huawei.micro.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据访问接口。
 *
 * @author Eric
 * @since 1.0.0
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据用户名查询用户。
     *
     * @param username 用户名
     * @return 用户实体
     */
    User selectUserByUsername(@Param("username") String username);

    /**
     * 统计用户名数量（含逻辑删除用户）。
     *
     * @param username      用户名
     * @param excludeUserId 排除的用户 ID（修改场景）
     * @return 用户名数量
     */
    int countByUsername(@Param("username") String username, @Param("excludeUserId") Long excludeUserId);

    /**
     * 根据用户 ID 查询关联角色列表。
     *
     * @param userId 用户 ID
     * @return 角色列表
     */
    List<Role> selectRoleByUserId(@Param("userId") Long userId);
}
