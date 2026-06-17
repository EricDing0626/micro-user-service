package com.huawei.micro.controller;

import com.huawei.micro.common.Result;
import com.huawei.micro.service.BaseDataService;
import com.huawei.micro.vo.BaseDataCreateVO;
import com.huawei.micro.vo.BaseDataDetailVO;
import com.huawei.micro.vo.BaseDataUpdateVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * 基础数据管理 REST 接口。
 *
 * @author Eric
 * @since 1.0.0
 */
@Api(tags = "基础数据管理")
@RestController
@RequestMapping("/api/base-data")
@RequiredArgsConstructor
@Validated
public class BaseDataController {

    private final BaseDataService baseDataService;

    /**
     * 新增基础数据。
     *
     * @param createVO 新增参数
     * @return 新记录 ID
     */
    @ApiOperation(value = "新增基础数据", notes = "创建一条基础数据字典项")
    @PostMapping
    public Result<Long> createBaseData(@Valid @RequestBody BaseDataCreateVO createVO) {
        Long id = baseDataService.createBaseData(createVO);
        return Result.success("新增基础数据成功", id);
    }

    /**
     * 修改基础数据。
     *
     * @param updateVO 修改参数
     * @return 操作结果
     */
    @ApiOperation(value = "修改基础数据", notes = "根据 ID 修改基础数据")
    @PutMapping
    public Result<Void> updateBaseData(@Valid @RequestBody BaseDataUpdateVO updateVO) {
        baseDataService.updateBaseData(updateVO);
        return Result.success("修改基础数据成功", null);
    }

    /**
     * 根据 ID 删除基础数据。
     *
     * @param id 基础数据 ID
     * @return 操作结果
     */
    @ApiOperation(value = "删除基础数据", notes = "根据 ID 逻辑删除基础数据")
    @DeleteMapping("/{id}")
    public Result<Void> deleteBaseDataById(
            @ApiParam(value = "基础数据ID", required = true) @PathVariable Long id) {
        baseDataService.deleteBaseDataById(id);
        return Result.success("删除基础数据成功", null);
    }

    /**
     * 根据类型编码查询基础数据列表。
     *
     * @param typeCode 数据类型编码
     * @return 基础数据列表
     */
    @ApiOperation(value = "按类型查询基础数据", notes = "根据 typeCode 查询基础数据列表，按 sort 升序")
    @GetMapping("/type/{typeCode}")
    public Result<List<BaseDataDetailVO>> listBaseDataByTypeCode(
            @ApiParam(value = "数据类型编码", required = true) @PathVariable String typeCode) {
        List<BaseDataDetailVO> list = baseDataService.listBaseDataByTypeCode(typeCode);
        return Result.success(list);
    }
}
