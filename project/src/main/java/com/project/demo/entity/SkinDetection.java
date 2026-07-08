package com.project.demo.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;


/**
 * 皮肤检测：(SkinDetection)表实体类
 *
 */
@TableName("`skin_detection`")
@Data
@EqualsAndHashCode(callSuper = false)
public class SkinDetection implements Serializable {

    // SkinDetection编号
    @TableId(value = "skin_detection_id", type = IdType.AUTO)
    private Integer skin_detection_id;

    // 注册用户
    @TableField(value = "`registered_user`")
    private Integer registered_user;
    // 用户姓名
    @TableField(value = "`user_name`")
    private String user_name;
    // 用户性别
    @TableField(value = "`user_gender`")
    private String user_gender;
    // 用户年龄
    @TableField(value = "`user_age`")
    private Double user_age;
    // 录入日期
    @TableField(value = "`entry_date`")
    private Timestamp entry_date;
    // 检测图片
    @TableField(value = "`detection_picture`")
    private String detection_picture;
    // 皮肤情况
    @TableField(value = "`skin_condition`")
    private String skin_condition;
    // 检测结果
    @TableField(value = "`test_results`")
    private String test_results;
    // 检测说明
    @TableField(value = "`test_description`")
    private String test_description;























	// 更新时间
    @TableField(value = "update_time")
    private Timestamp update_time;

    // 创建时间
    @TableField(value = "create_time")
    private Timestamp create_time;

}
