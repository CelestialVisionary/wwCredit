package com.wwfinance.controller.api;

import com.wwfinance.entity.Announcement;
import com.wwfinance.service.AnnouncementService;
import com.wwfinance.common.result.PccAjaxResult;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 公告表 前端控制器
 * </p>
 */
@RestController
@RequestMapping("/api/core/announcement")
public class AnnouncementController {

    @Autowired
    private AnnouncementService announcementService;

    @Operation(summary = "获取最新公告列表")
    @GetMapping("/latest")
    public PccAjaxResult getLatestAnnouncements() {
        List<Announcement> announcements = announcementService.getLatestAnnouncements();
        return new PccAjaxResult(200, "获取成功", announcements);
    }
}
