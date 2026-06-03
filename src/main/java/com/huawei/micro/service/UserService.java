package com.huawei.micro.service;

import com.huawei.micro.vo.UserCreateVO;
import com.huawei.micro.vo.UserDetailVO;
import com.huawei.micro.vo.UserUpdateVO;

/**
 * 用户服务接口
 */
public interface UserService {

    Long createUser(UserCreateVO createVO);

    UserDetailVO getUserById(Long id);

    void updateUser(UserUpdateVO updateVO);

    void deleteUserById(Long id);
}
