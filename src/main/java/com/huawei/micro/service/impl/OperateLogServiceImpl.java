package com.huawei.micro.service.impl;

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

import java.time.LocalDateTime;

/**
 * 操作日志业务服务实现类。
 *
 * @author Eric
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class OperateLogServiceImpl implements OperateLogService {

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
