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
 * 借款人
 * </p>
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("borrower")
public class Borrower implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 姓名
     */
    @TableField("name")
    private String name;

    /**
     * 身份证号
     */
    @TableField("id_card")
    private String idCard;

    /**
     * 手机
     */
    @TableField("mobile")
    private String mobile;

    /**
     * 性别（1：男 0：女）
     */
    @TableField("sex")
    private Integer sex;

    /**
     * 年龄
     */
    @TableField("age")
    private Integer age;

    /**
     * 学历
     */
    @TableField("education")
    private Integer education;

    /**
     * 是否结婚（1：是 0：否）
     */
    @TableField("is_marry")
    private Boolean marry;

    /**
     * 行业
     */
    @TableField("industry")
    private Integer industry;

    /**
     * 月收入
     */
    @TableField("income")
    private Integer income;

    /**
     * 还款来源
     */
    @TableField("return_source")
    private Integer returnSource;

    /**
     * 联系人名称
     */
    @TableField("contacts_name")
    private String contactsName;

    /**
     * 联系人手机
     */
    @TableField("contacts_mobile")
    private String contactsMobile;

    /**
     * 联系人关系
     */
    @TableField("contacts_relation")
    private Integer contactsRelation;

    /**
     * 状态（0：未认证，1：认证中， 2：认证通过， -1：认证失败）
     */
    @TableField("status")
    private Integer status;

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
     * 逻辑删除(1:已删除，0:未删除)
     */
    @TableField("is_deleted")
    private Boolean deleted;


}
