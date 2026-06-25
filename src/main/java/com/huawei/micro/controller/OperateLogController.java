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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

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
     * 查询最近的操作日志。
     *
     * @param operator          操作人（可选）
     * @param requestPathPrefix 请求路径前缀（可选）
     * @param limit             返回条数，默认 20，最大 100
     * @return 操作日志列表
     */
    @ApiOperation(value = "查询最近操作日志", notes = "按操作人、路径前缀筛选最近日志，用于跨模块联调验证")
    @GetMapping("/recent")
    public Result<List<OperateLogDetailVO>> listRecentOperateLogs(
            @ApiParam(value = "操作人") @RequestParam(required = false) String operator,
            @ApiParam(value = "请求路径前缀") @RequestParam(required = false) String requestPathPrefix,
            @ApiParam(value = "返回条数") @RequestParam(required = false) Integer limit) {
        List<OperateLogDetailVO> list = operateLogService.listRecentOperateLogs(operator, requestPathPrefix, limit);
        return Result.success(list);
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
