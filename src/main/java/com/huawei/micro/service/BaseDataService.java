package com.huawei.micro.service;

import com.huawei.micro.vo.BaseDataCreateVO;
import com.huawei.micro.vo.BaseDataDetailVO;
import com.huawei.micro.vo.BaseDataUpdateVO;

import java.util.List;

/**
 * 基础数据业务服务接口。
 *
 * @author Eric
 * @since 1.0.0
 */
public interface BaseDataService {

    /**
     * 新增基础数据。
     *
     * @param createVO 新增参数
     * @return 新记录 ID
     */
    Long createBaseData(BaseDataCreateVO createVO);

    /**
     * 修改基础数据。
     *
     * @param updateVO 修改参数
     */
    void updateBaseData(BaseDataUpdateVO updateVO);

    /**
     * 根据 ID 删除基础数据。
     *
     * @param id 基础数据 ID
     */
    void deleteBaseDataById(Long id);

    /**
     * 根据类型编码查询基础数据列表。
     *
     * @param typeCode 数据类型编码
     * @return 基础数据列表
     */
    List<BaseDataDetailVO> listBaseDataByTypeCode(String typeCode);
}
