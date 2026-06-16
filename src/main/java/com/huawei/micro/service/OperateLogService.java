package com.huawei.micro.service;

import com.huawei.micro.vo.OperateLogCreateVO;
import com.huawei.micro.vo.OperateLogDetailVO;

/**
 * 操作日志业务服务接口。
 *
 * @author Eric
 * @since 1.0.0
 */
public interface OperateLogService {

    /**
     * 新增操作日志。
     *
     * @param createVO 操作日志新增参数
     * @return 新日志 ID
     */
    Long createOperateLog(OperateLogCreateVO createVO);

    /**
     * 根据 ID 查询操作日志。
     *
     * @param id 日志 ID
     * @return 操作日志详情
     */
    OperateLogDetailVO getOperateLogById(Long id);

    /**
     * 根据 ID 删除操作日志。
     *
     * @param id 日志 ID
     */
    void deleteOperateLogById(Long id);
}
