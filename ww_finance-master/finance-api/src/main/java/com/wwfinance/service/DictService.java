package com.wwfinance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.wwfinance.entity.Dict;
import com.wwfinance.entity.dto.ExcelDictDTO;

import java.util.List;

/**
 * <p>
 * 数据字典 服务类
 * </p>
 *
 */
public interface DictService extends IService<Dict> {

    List<Dict> findByDictCode(String dictCode);

    List<Dict> listByParentId(Long parentId);

    List<ExcelDictDTO> listDictData();

    String getNameByParentDictCodeAndValue(String dictCode, Integer value);
}
