package com.huawei.micro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huawei.micro.common.ResultCode;
import com.huawei.micro.entity.BaseData;
import com.huawei.micro.exception.BusinessException;
import com.huawei.micro.mapper.BaseDataMapper;
import com.huawei.micro.service.BaseDataService;
import com.huawei.micro.vo.BaseDataCreateVO;
import com.huawei.micro.vo.BaseDataDetailVO;
import com.huawei.micro.vo.BaseDataUpdateVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 基础数据业务服务实现类。
 *
 * @author Eric
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class BaseDataServiceImpl implements BaseDataService {

    private static final int DEFAULT_SORT = 0;
    private static final int STATUS_ENABLED = 1;

    private final BaseDataMapper baseDataMapper;

    /**
     * 新增基础数据。
     *
     * @param createVO 新增参数
     * @return 新记录 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createBaseData(BaseDataCreateVO createVO) {
        validateTypeDataUnique(createVO.getTypeCode(), createVO.getDataCode(), null);

        BaseData baseData = new BaseData();
        BeanUtils.copyProperties(createVO, baseData);
        if (baseData.getSort() == null) {
            baseData.setSort(DEFAULT_SORT);
        }
        if (baseData.getStatus() == null) {
            baseData.setStatus(STATUS_ENABLED);
        }
        LocalDateTime now = LocalDateTime.now();
        baseData.setCreateTime(now);
        baseData.setUpdateTime(now);
        baseDataMapper.insert(baseData);
        return baseData.getId();
    }

    /**
     * 修改基础数据。
     *
     * @param updateVO 修改参数
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBaseData(BaseDataUpdateVO updateVO) {
        BaseData baseData = validateBaseDataExists(updateVO.getId());

        String typeCode = StringUtils.hasText(updateVO.getTypeCode())
                ? updateVO.getTypeCode() : baseData.getTypeCode();
        String dataCode = StringUtils.hasText(updateVO.getDataCode())
                ? updateVO.getDataCode() : baseData.getDataCode();
        validateTypeDataUnique(typeCode, dataCode, baseData.getId());

        if (StringUtils.hasText(updateVO.getTypeCode())) {
            baseData.setTypeCode(updateVO.getTypeCode());
        }
        if (StringUtils.hasText(updateVO.getDataCode())) {
            baseData.setDataCode(updateVO.getDataCode());
        }
        if (StringUtils.hasText(updateVO.getDataName())) {
            baseData.setDataName(updateVO.getDataName());
        }
        if (updateVO.getSort() != null) {
            baseData.setSort(updateVO.getSort());
        }
        if (updateVO.getStatus() != null) {
            baseData.setStatus(updateVO.getStatus());
        }
        baseData.setUpdateTime(LocalDateTime.now());
        baseDataMapper.updateById(baseData);
    }

    /**
     * 根据 ID 删除基础数据（逻辑删除）。
     *
     * @param id 基础数据 ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBaseDataById(Long id) {
        validateBaseDataExists(id);
        baseDataMapper.deleteById(id);
    }

    /**
     * 根据类型编码查询基础数据列表。
     *
     * @param typeCode 数据类型编码
     * @return 基础数据列表
     */
    @Override
    public List<BaseDataDetailVO> listBaseDataByTypeCode(String typeCode) {
        if (!StringUtils.hasText(typeCode)) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "数据类型编码不能为空");
        }

        LambdaQueryWrapper<BaseData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseData::getTypeCode, typeCode)
                .orderByAsc(BaseData::getSort)
                .orderByAsc(BaseData::getId);
        return baseDataMapper.selectList(queryWrapper).stream()
                .map(this::convertToDetailVO)
                .collect(Collectors.toList());
    }

    private BaseData validateBaseDataExists(Long id) {
        if (id == null) {
            throw new BusinessException(ResultCode.BAD_REQUEST, "基础数据ID不能为空");
        }
        BaseData baseData = baseDataMapper.selectById(id);
        if (baseData == null) {
            throw new BusinessException(ResultCode.NOT_FOUND, "基础数据不存在，ID=" + id);
        }
        return baseData;
    }

    private void validateTypeDataUnique(String typeCode, String dataCode, Long excludeId) {
        LambdaQueryWrapper<BaseData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BaseData::getTypeCode, typeCode)
                .eq(BaseData::getDataCode, dataCode);
        if (excludeId != null) {
            queryWrapper.ne(BaseData::getId, excludeId);
        }
        Long count = baseDataMapper.selectCount(queryWrapper);
        if (count != null && count > 0) {
            throw new BusinessException(ResultCode.BAD_REQUEST,
                    "同类型下数据编码已存在: " + typeCode + "/" + dataCode);
        }
    }

    private BaseDataDetailVO convertToDetailVO(BaseData baseData) {
        BaseDataDetailVO detailVO = new BaseDataDetailVO();
        BeanUtils.copyProperties(baseData, detailVO);
        return detailVO;
    }
}
