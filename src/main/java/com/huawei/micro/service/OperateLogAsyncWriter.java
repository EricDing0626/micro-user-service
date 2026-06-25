package com.huawei.micro.service;

import com.huawei.micro.vo.OperateLogCreateVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * 操作日志异步写入服务，避免阻塞主业务接口。
 *
 * @author Eric
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperateLogAsyncWriter {

    private final OperateLogService operateLogService;

    /**
     * 异步写入操作日志。
     *
     * @param createVO 操作日志新增参数
     */
    @Async("operateLogExecutor")
    public void writeOperateLogAsync(OperateLogCreateVO createVO) {
        try {
            operateLogService.createOperateLog(createVO);
        } catch (Exception ex) {
            log.warn("异步写入操作日志失败: path={}, method={}",
                    createVO.getRequestPath(), createVO.getRequestMethod(), ex);
        }
    }
}
