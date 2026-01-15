package com.wwfinance.service.impl;

import com.wwfinance.entity.Announcement;
import com.wwfinance.mapper.AnnouncementMapper;
import com.wwfinance.service.AnnouncementService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 公告表 服务实现类
 * </p>
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements AnnouncementService {

    @Override
    public List<Announcement> getLatestAnnouncements() {
        QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("is_top")
                   .orderByDesc("publish_date")
                   .last("limit 5");
        return baseMapper.selectList(queryWrapper);
    }
}
