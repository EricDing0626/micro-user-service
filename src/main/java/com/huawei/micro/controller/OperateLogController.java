package com.huawei.micro.controller;

import com.huawei.micro.common.Result;
import com.huawei.micro.service.OperateLogService;
import com.huawei.micro.vo.OperateLogCreateVO;
import com.huawei.micro.vo.OperateLogDetailVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 操作日志管理 REST 接口。
 *
 * @author Eric
 * @since 1.0.0
 */
@Api(tags = "操作日志管理")
@RestController
@RequestMapping("/api/operate-logs")
@RequiredArgsConstructor
@Validated
public class OperateLogController {

    private final OperateLogService operateLogService;

    /**
     * 新增操作日志。
     *
     * @param createVO 操作日志新增参数
     * @return 新日志 ID
     */
    @ApiOperation(value = "新增操作日志", notes = "手动写入一条操作日志记录")
    @PostMapping
    public Result<Long> createOperateLog(@Valid @RequestBody OperateLogCreateVO createVO) {
        Long logId = operateLogService.createOperateLog(createVO);
        return Result.success("新增操作日志成功", logId);
    }

    /**
     * 根据 ID 查询操作日志。
     *
     * @param id 日志 ID
     * @return 操作日志详情
     */
    @ApiOperation(value = "查询操作日志", notes = "根据日志 ID 查询详情")
    @GetMapping("/{id}")
    public Result<OperateLogDetailVO> getOperateLogById(
            @ApiParam(value = "日志ID", required = true) @PathVariable Long id) {
        OperateLogDetailVO detailVO = operateLogService.getOperateLogById(id);
        return Result.success(detailVO);
    }

    /**
     * 根据 ID 删除操作日志。
     *
     * @param id 日志 ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除操作日志", notes = "根据日志 ID 物理删除")
    @DeleteMapping("/{id}")
    public Result<Void> deleteOperateLogById(
            @ApiParam(value = "日志ID", required = true) @PathVariable Long id) {
        operateLogService.deleteOperateLogById(id);
        return Result.success("删除操作日志成功", null);
    }
}
