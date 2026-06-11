package com.huawei.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huawei.micro.common.ResultCode;
import com.huawei.micro.config.PageProperties;
import com.huawei.micro.entity.Role;
import com.huawei.micro.entity.User;
import com.huawei.micro.entity.UserRole;
import com.huawei.micro.exception.BusinessException;
import com.huawei.micro.mapper.RoleMapper;
import com.huawei.micro.mapper.UserMapper;
import com.huawei.micro.mapper.UserRoleMapper;
import com.huawei.micro.service.UserService;
import com.huawei.micro.util.Md5Util;
import com.huawei.micro.vo.PageResultVO;
import com.huawei.micro.vo.UserBatchDeleteResultVO;
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
import java.util.stream.Collectors;

/**
 * 用户业务服务实现类。
 *
 * @author Eric
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int USER_STATUS_ENABLED = 1;

    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final UserRoleMapper userRoleMapper;
    private final PageProperties pageProperties;

    /**
     * 新增用户并分配角色。
     *
     * @param createVO 用户新增参数
     * @return 新用户 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createUser(UserCreateVO createVO) {
        validateUsernameUnique(createVO.getUsername(), null);
        validatePassword(createVO.getPassword());

        User user = new User();
        BeanUtils.copyProperties(createVO, user);
        user.setPassword(Md5Util.encrypt(createVO.getPassword()));
        if (user.getStatus() == null) {
            user.setStatus(USER_STATUS_ENABLED);
        }
        user.setCreateTime(LocalDateTime.now());
        user.setUpdateTime(LocalDateTime.now());

        userMapper.insert(user);
        assignUserRoles(user.getId(), createVO.getRoleIds());
        return user.getId();
    }

    /**
     * 根据 ID 查询用户详情（含角色）。
     *
     * @param id 用户 ID
     * @return 用户详情
     */
    @Override
    public UserDetailVO getUserById(Long id) {
        User user = validateUserExists(id);
        return convertToUserDetailVO(user);
    }

    /**
     * 分页查询用户列表，支持用户名模糊搜索。
     *
     * @param pageNum  页码
     * @param pageSize 每页条数
     * @param username 用户名（模糊匹配）
     * @return 分页结果
     */
    @Override
    public PageResultVO<UserDetailVO> listUsers(Integer pageNum, Integer pageSize, String username) {
        int currentPageNum = normalizePageNum(pageNum);
        int currentPageSize = normalizePageSize(pageSize);

        Page<User> page = new Page<>(currentPageNum, currentPageSize);
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(username)) {
            queryWrapper.like(User::getUsername, username);
        }
        queryWrapper.orderByDesc(User::getCreateTime);

        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        List<UserDetailVO> records = userPage.getRecords().stream()
                .map(this::convertToUserDetailVO)
                .collect(Collectors.toList());
        return buildPageResult(userPage, records);
    }

    /**
     * 修改用户信息及角色。
     *
     * @param updateVO 用户修改参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(UserUpdateVO updateVO) {
        User user = validateUserExists(updateVO.getId());

        if (StringUtils.hasText(updateVO.getUsername())) {
            validateUsernameUnique(updateVO.getUsername(), user.getId());
            user.setUsername(updateVO.getUsername());
        }
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

    /**
     * 删除用户并级联删除角色关联。
     *
     * @param id 用户 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserById(Long id) {
        validateUserExists(id);
        doDeleteUser(id);
    }

    /**
     * 批量删除用户，部分不存在时不影响其他用户删除。
     *
     * @param ids 用户 ID 列表
     * @return 批量删除结果
     */
    @Override
    public UserBatchDeleteResultVO batchDeleteUsers(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户ID列表不能为空");
        }

        UserBatchDeleteResultVO result = new UserBatchDeleteResultVO();
        for (Long id : ids) {
            if (id == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "用户ID不能为空");
            }
            if (userMapper.selectById(id) == null) {
                result.getFailedIds().add(id);
                continue;
            }
            doDeleteUser(id);
            result.getSuccessIds().add(id);
        }

        result.setSuccessCount(result.getSuccessIds().size());
        result.setFailedCount(result.getFailedIds().size());
        return result;
    }

    /**
     * 校验用户是否存在。
     *
     * @param id 用户 ID
     * @return 用户实体
     */
    private User validateUserExists(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户ID不能为空");
        }
        User user = userMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "用户不存在，ID=" + id);
        }
        return user;
    }

    /**
     * 校验用户名非空。
     *
     * @param username 用户名
     */
    private void validateUsername(String username) {
        if (!StringUtils.hasText(username)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名不能为空");
        }
    }

    /**
     * 校验用户名唯一性。
     *
     * @param username      用户名
     * @param excludeUserId 排除的用户 ID（修改场景）
     */
    private void validateUsernameUnique(String username, Long excludeUserId) {
        validateUsername(username);
        if (userMapper.countByUsername(username, excludeUserId) > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "用户名已存在");
        }
    }

    /**
     * 校验密码格式。
     *
     * @param password 明文密码
     */
    private void validatePassword(String password) {
        if (!StringUtils.hasText(password)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "密码不能为空");
        }
        if (password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "密码长度必须在6-20位之间");
        }
    }

    /**
     * 为用户分配角色。
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 列表
     */
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

    /**
     * 删除用户及其角色关联。
     *
     * @param userId 用户 ID
     */
    private void doDeleteUser(Long userId) {
        deleteUserRoles(userId);
        userMapper.deleteById(userId);
    }

    /**
     * 删除用户角色关联。
     *
     * @param userId 用户 ID
     */
    private void deleteUserRoles(Long userId) {
        userRoleMapper.delete(new LambdaQueryWrapper<UserRole>().eq(UserRole::getUserId, userId));
    }

    /**
     * 替换用户角色关联。
     *
     * @param userId  用户 ID
     * @param roleIds 角色 ID 列表
     */
    private void replaceUserRoles(Long userId, List<Long> roleIds) {
        deleteUserRoles(userId);
        assignUserRoles(userId, roleIds);
    }

    /**
     * 校验角色 ID 列表有效性。
     *
     * @param roleIds 角色 ID 列表
     */
    private void validateRoleIds(List<Long> roleIds) {
        for (Long roleId : roleIds) {
            if (roleId == null) {
                throw new BusinessException(ResultCode.BAD_REQUEST, "角色ID不能为空");
            }
            Role role = roleMapper.selectById(roleId);
            if (role == null) {
                throw new BusinessException(ResultCode.NOT_FOUND, "角色不存在，ID=" + roleId);
            }
        }
    }

    /**
     * 规范化页码。
     *
     * @param pageNum 页码
     * @return 有效页码
     */
    private int normalizePageNum(Integer pageNum) {
        if (pageNum == null || pageNum < 1) {
            return pageProperties.getDefaultPageNum();
        }
        return pageNum;
    }

    /**
     * 规范化每页条数。
     *
     * @param pageSize 每页条数
     * @return 有效每页条数
     */
    private int normalizePageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 1) {
            return pageProperties.getDefaultPageSize();
        }
        return Math.min(pageSize, pageProperties.getMaxPageSize());
    }

    /**
     * 转换用户实体为详情 VO。
     *
     * @param user 用户实体
     * @return 用户详情
     */
    private UserDetailVO convertToUserDetailVO(User user) {
        UserDetailVO detailVO = new UserDetailVO();
        BeanUtils.copyProperties(user, detailVO);
        detailVO.setRoles(userMapper.selectRoleByUserId(user.getId()));
        return detailVO;
    }

    /**
     * 构建分页结果。
     *
     * @param userPage 分页查询结果
     * @param records  记录列表
     * @return 分页 VO
     */
    private PageResultVO<UserDetailVO> buildPageResult(Page<User> userPage, List<UserDetailVO> records) {
        PageResultVO<UserDetailVO> pageResult = new PageResultVO<>();
        pageResult.setRecords(records);
        pageResult.setTotal(userPage.getTotal());
        pageResult.setPageNum(userPage.getCurrent());
        pageResult.setPageSize(userPage.getSize());
        pageResult.setPages(userPage.getPages());
        return pageResult;
    }
}
