package com.wwfinance.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 数据字典
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("dict")
public class Dict implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 上级id
     */
    @TableField("parent_id")
    private Long parentId;

    /**
     * 名称
     */
    @TableField("name")
    private String name;

    /**
     * 值
     */
    @TableField("value")
    private Integer value;

    /**
     * 编码
     */
    @TableField("dict_code")
    private String dictCode;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 删除标记（0:不可用 1:可用）
     */
    @TableField("is_deleted")
    private Boolean deleted;

    @TableField(exist = false) //表达逻辑概念的属性，和物理表没有关系，当前字段不存在于物理表中
    private boolean hasChildren;

}
