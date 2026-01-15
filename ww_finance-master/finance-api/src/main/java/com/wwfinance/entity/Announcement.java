package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 公告表
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("announcement")
public class Announcement implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 发布时间
     */
    @TableField("publish_date")
    private LocalDateTime publishDate;

    /**
     * 分类
     */
    @TableField("category")
    private String category;

    /**
     * 是否置顶 0：否 1：是
     */
    @TableField("is_top")
    private Integer isTop;

}
