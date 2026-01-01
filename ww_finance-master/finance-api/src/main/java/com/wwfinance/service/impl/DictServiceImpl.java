package com.wwfinance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.wwfinance.entity.Dict;
import com.wwfinance.entity.dto.ExcelDictDTO;
import com.wwfinance.mapper.DictMapper;
import com.wwfinance.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 数据字典 服务实现类
 * </p>
 */
@Service
@Slf4j
public class DictServiceImpl extends ServiceImpl<DictMapper, Dict> implements DictService {

    @Resource
    private RedisTemplate<String, List<Dict>> redisTemplate;

    @Override
    public List<Dict> findByDictCode(String dictCode) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        return this.listByParentId(dict.getId());
    }

    @Override
    public List<Dict> listByParentId(Long parentId) {

        try {
            //首先查询redis中是否存在数据列表
            List<Dict> dictList = redisTemplate.opsForValue().get("xx:core:dictList:" + parentId);
            if(dictList != null){
                //如果存在则从redis中直接返回数据列表
                log.info("从redis中获取数据列表");
                return dictList;
            }
        } catch (Exception e) {
            log.error("redis服务器异常:" + ExceptionUtils.getStackTrace(e));
        }

        //如果不存在则查询数据库
        log.info("从数据库中获取数据列表");
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", parentId);
        List<Dict> dictList = baseMapper.selectList(dictQueryWrapper);
        //填充hashChildren字段
        dictList.forEach(dict -> {
            //判断当前节点是否有子节点，找到当前的dict下级有没有子节点
            boolean hasChildren = this.hasChildren(dict.getId());
            dict.setHasChildren(hasChildren);
        });

        try {
            //将数据存入redis
            log.info("将数据存入redis");
            redisTemplate.opsForValue().set("xx:core:dictList:" + parentId, dictList, 5, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("redis服务器异常:" + ExceptionUtils.getStackTrace(e));
        }

        //返回数据列表
        return dictList;
    }

    @Override
    public List<ExcelDictDTO> listDictData() {

        List<Dict> dictList = baseMapper.selectList(null);
        //创建ExcelDictDTO列表，将Dict列表转换成ExcelDictDTO列表
        ArrayList<ExcelDictDTO> excelDictDTOList = new ArrayList<>(dictList.size());
        dictList.forEach(dict -> {

            ExcelDictDTO excelDictDTO = new ExcelDictDTO();
            BeanUtils.copyProperties(dict, excelDictDTO);
            excelDictDTOList.add(excelDictDTO);
        });
        return excelDictDTOList;
    }

    private boolean hasChildren(Long id){
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper.eq("parent_id", id);
        Long count = baseMapper.selectCount(dictQueryWrapper);
        if(count > 0){
            return true;
        }
        return false;
    }

    @Override
    public String getNameByParentDictCodeAndValue(String dictCode, Integer value) {
        QueryWrapper<Dict> dictQueryWrapper = new QueryWrapper<Dict>();
        dictQueryWrapper.eq("dict_code", dictCode);
        Dict parentDict = baseMapper.selectOne(dictQueryWrapper);
        if(parentDict == null) {
            return "";
        }
        dictQueryWrapper = new QueryWrapper<>();
        dictQueryWrapper
                .eq("parent_id", parentDict.getId())
                .eq("value", value);
        Dict dict = baseMapper.selectOne(dictQueryWrapper);
        if(dict == null) {
            return "";
        }
        return dict.getName();
    }
}
