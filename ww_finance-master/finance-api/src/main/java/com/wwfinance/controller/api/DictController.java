package com.wwfinance.controller.api;


import com.wwfinance.entity.Dict;
import com.wwfinance.service.DictService;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 */
@RestController
@RequestMapping("/api/core/dict")
public class DictController {

    @Resource
    private DictService dictService;

    @Operation(summary = "根据dictCode获取下级节点")
    @GetMapping("/findByDictCode/{dictCode}")
    public PccAjaxResult findByDictCode(
            @Parameter(description = "节点编码", required = true)
            @PathVariable String dictCode) {
        List<Dict> list = dictService.findByDictCode(dictCode);
        return new PccAjaxResult(200,"获取成功", list);
    }
}


