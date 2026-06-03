package com.huawei.micro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huawei.micro.entity.Role;
import com.huawei.micro.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户 Mapper
 */
public interface UserMapper extends BaseMapper<User> {

    User selectUserByUsername(@Param("username") String username);

    List<Role> selectRoleByUserId(@Param("userId") Long userId);
}
