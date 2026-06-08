package com.huawei.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huawei.micro.entity.Role;
import com.huawei.micro.entity.User;
import com.huawei.micro.entity.UserRole;
import com.huawei.micro.exception.BusinessException;
import com.huawei.micro.mapper.RoleMapper;
import com.huawei.micro.mapper.UserMapper;
import com.huawei.micro.mapper.UserRoleMapper;
import com.huawei.micro.service.UserService;
import com.huawei.micro.util.Md5Util;
import com.huawei.micro.vo.UserCreateVO;
import com.huawei.micro.vo.UserDetailVO;
import com.huawei.micro.vo.UserUpdateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户服务实现类
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateVO createVO) {
        validateUsername(createVO.getUsername());
        validatePassword(createVO.getPassword());

        User existUser = userMapper.selectUserByUsername(createVO.getUsername());
        if (existUser != null) {
            throw new BusinessException("用户名已存在");
        }

        User user = new User();
        BeanUtils.copyProperties(createVO, user);
        user.setPassword(Md5Util.encrypt(createVO.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(1);
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        assignUserRoles(user.getId(), createVO.getRoleIds());
        return user.getId();
    }

    @Override
    public UserDetailVO getUserById(Long id) {
        User user = validateUserExists(id);

        UserDetailVO detailVO = new UserDetailVO();
        BeanUtils.copyProperties(user, detailVO);
        detailVO.setRoles(userMapper.selectRoleByUserId(id));
        return detailVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateVO updateVO) {
        User user = validateUserExists(updateVO.getId());

        if (StringUtils.hasText(updateVO.getPassword())) {
            validatePassword(updateVO.getPassword());
            user.setPassword(Md5Util.encrypt(updateVO.getPassword()));
        }
        if (updateVO.getEmail() != null) {
            user.setEmail(updateVO.getEmail());
        }
        if (updateVO.getPhone() != null) {
            user.setPhone(updateVO.getPhone());
        }
        if (updateVO.getStatus() != null) {
            user.setStatus(updateVO.getStatus());
        }
        user.setUpdateTime(LocalDateTime.now());

        userMapper.updateById(user);
        if (updateVO.getRoleIds() != null) {
            replaceUserRoles(user.getId(), updateVO.getRoleIds());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserById(Long id) {
        validateUserExists(id);
        userMapper.deleteById(id);
    }

    private User validateUserExists(Long id) {
        if (id == null) {
            throw new BusinessException("用户ID不能为空");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException("用户不存在，ID=" + id);
        }
        return user;
    }

    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new BusinessException("用户名不能为空");
        }
    }

    private void validatePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new BusinessException("密码不能为空");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new BusinessException("密码长度必须在6-20位之间");
        }
    }

    private void assignUserRoles(Long userId, List<Long> roleIds) {
        if (CollectionUtils.isEmpty(roleIds)) {
            return;
        }
        validateRoleIds(roleIds);
        LocalDateTime now = LocalDateTime.now();
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRole.setCreateTime(now);
            userRoleMapper.insert(userRole);
        }
    }

    private void replaceUserRoles(Long userId, List<Long> roleIds) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
        assignUserRoles(userId, roleIds);
    }

    private void validateRoleIds(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            if (roleId == null) {
                throw new BusinessException("角色ID不能为空");
            }
            Role role = roleMapper.selectById(roleId);
            if (role == null) {
                throw new BusinessException("角色不存在，ID=" + roleId);
            }
        }
    }
}
