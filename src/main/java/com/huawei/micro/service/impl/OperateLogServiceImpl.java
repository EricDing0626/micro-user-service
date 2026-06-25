package com.huawei.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huawei.micro.common.ResultCode;
import com.huawei.micro.entity.OperateLog;
import com.huawei.micro.exception.BusinessException;
import com.huawei.micro.mapper.OperateLogMapper;
import com.huawei.micro.service.OperateLogService;
import com.huawei.micro.vo.OperateLogCreateVO;
import com.huawei.micro.vo.OperateLogDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 操作日志业务服务实现类。
 *
 * @author Eric
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class OperateLogServiceImpl implements OperateLogService {

    private static final int DEFAULT_RECENT_LIMIT = 20;
    private static final int MAX_RECENT_LIMIT = 100;

    private final OperateLogMapper operateLogMapper;

    /**
     * 新增操作日志。
     *
     * @param createVO 操作日志新增参数
     * @return 新日志 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOperateLog(OperateLogCreateVO createVO) {
        OperateLog operateLog = new OperateLog();
        BeanUtils.copyProperties(createVO, operateLog);
        if (operateLog.getOperateTime() == null) {
            operateLog.setOperateTime(LocalDateTime.now());
        }
        operateLog.setCreateTime(LocalDateTime.now());
        operateLogMapper.insert(operateLog);
        return operateLog.getId();
    }

    /**
     * 根据 ID 查询操作日志。
     *
     * @param id 日志 ID
     * @return 操作日志详情
     */
    @Override
    public OperateLogDetailVO getOperateLogById(Long id) {
        OperateLog operateLog = validateOperateLogExists(id);
        return convertToDetailVO(operateLog);
    }

    /**
     * 根据 ID 删除操作日志。
     *
     * @param id 日志 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteOperateLogById(Long id) {
        validateOperateLogExists(id);
        operateLogMapper.deleteById(id);
    }

    /**
     * 查询最近的操作日志，用于联调验证。
     *
     * @param operator           操作人（可选）
     * @param requestPathPrefix  请求路径前缀（可选）
     * @param limit              返回条数上限
     * @return 操作日志列表
     */
    @Override
    public List<OperateLogDetailVO> listRecentOperateLogs(String operator, String requestPathPrefix, Integer limit) {
        int queryLimit = limit == null ? DEFAULT_RECENT_LIMIT : limit;
        if (queryLimit <= 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "limit 必须大于 0");
        }
        if (queryLimit > MAX_RECENT_LIMIT) {
            queryLimit = MAX_RECENT_LIMIT;
        }

        LambdaQueryWrapper<OperateLog> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(operator)) {
            queryWrapper.eq(OperateLog::getOperator, operator);
        }
        if (StringUtils.hasText(requestPathPrefix)) {
            queryWrapper.likeRight(OperateLog::getRequestPath, requestPathPrefix);
        }
        queryWrapper.orderByDesc(OperateLog::getId).last("LIMIT " + queryLimit);

        return operateLogMapper.selectList(queryWrapper).stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());
    }

    private OperateLog validateOperateLogExists(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "日志ID不能为空");
        }
        OperateLog operateLog = operateLogMapper.selectById(id);
        if (operateLog == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "操作日志不存在，ID=" + id);
        }
        return operateLog;
    }

    private OperateLogDetailVO convertToDetailVO(OperateLog operateLog) {
        OperateLogDetailVO detailVO = new OperateLogDetailVO();
        BeanUtils.copyProperties(operateLog, detailVO);
        return detailVO;
    }
}
