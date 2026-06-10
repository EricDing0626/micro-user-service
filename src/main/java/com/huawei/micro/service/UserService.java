package com.huawei.micro.service;

import com.huawei.micro.vo.PageResultVO;
import com.huawei.micro.vo.UserBatchDeleteResultVO;
import com.huawei.micro.vo.UserCreateVO;
import com.huawei.micro.vo.UserDetailVO;
import com.huawei.micro.vo.UserUpdateVO;

import java.util.List;

/**
 * 用户服务接口
 */
public interface UserService {

    Long createUser(UserCreateVO createVO);

    UserDetailVO getUserById(Long id);

    PageResultVO<UserDetailVO> listUsers(Integer pageNum, Integer pageSize, String username);

    void updateUser(UserUpdateVO updateVO);

    void deleteUserById(Long id);

    UserBatchDeleteResultVO batchDeleteUsers(List<Long> ids);
}
