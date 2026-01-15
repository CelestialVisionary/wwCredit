package com.wwfinance.service;

import com.wwfinance.entity.Announcement;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 公告表 服务类
 * </p>
 */
public interface AnnouncementService extends IService<Announcement> {
    /**
     * 获取最新公告列表
     * @return 公告列表
     */
    List<Announcement> getLatestAnnouncements();
}
