package com.wwfinance.controller.admin;

import com.wwfinance.common.result.PccAjaxResult;
import com.wwfinance.entity.IntegralGrade;
import com.wwfinance.service.IntegralGradeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/core/integralGrade")
public class AdminIntegralGradeController {
    //依赖业务层对象
    @Resource
    private IntegralGradeService integralGradeService;
    /**
     * 1.查询所有积分等级
     * @return
     */

    @Operation(summary = "积分等级列表")
    @GetMapping("/list")
    public PccAjaxResult listAll() {
        List<IntegralGrade> list = integralGradeService.list();
        return new PccAjaxResult(200, "获取积分列表", list);
    }

    @Operation(summary = "新增积分等级")
    @PostMapping("/save")
    public PccAjaxResult save(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.save(integralGrade);
        if(result)
            return new PccAjaxResult(200,"保存积分成功");
        else
            return new PccAjaxResult(500,"保存积分失败");
    }

    @Operation(summary = "根据id删除数据记录", description = "逻辑删除数据记录")
    @PostMapping("/removeById")
    public PccAjaxResult removeById(@RequestParam Long id) {
        boolean result = integralGradeService.removeById(id);
        if(result)
            return new PccAjaxResult(200,"删除积分成功");
        else
            return new PccAjaxResult(500,"删除积分失败");
    }

    @Operation(summary = "根据id获取积分等级")
    @PostMapping("/getById")
    public PccAjaxResult getById(@RequestParam Long id) {
        IntegralGrade integralGrade = integralGradeService.getById(id);
        if(integralGrade != null)
            return new PccAjaxResult(200, "获取积分等级成功", integralGrade);
        else
            return new PccAjaxResult(500, "未找到匹配的积分等级");
    }

    @Operation(summary = "根据id更新积分等级")
    @PostMapping("/update")
    public PccAjaxResult updateById(@RequestBody IntegralGrade integralGrade) {
        boolean result = integralGradeService.updateById(integralGrade);
        if(result)
            return new PccAjaxResult(200, "更新积分等级成功");
        else
            return new PccAjaxResult(500, "更新积分等级失败");
    }
}
