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
 * 医院信息：(HospitalInformation)表实体类
 *
 */
@TableName("`hospital_information`")
@Data
@EqualsAndHashCode(callSuper = false)
public class HospitalInformation implements Serializable {

    // HospitalInformation编号
    @TableId(value = "hospital_information_id", type = IdType.AUTO)
    private Integer hospital_information_id;

    // 医院名称
    @TableField(value = "`hospital_name`")
    private String hospital_name;
    // 医院地址
    @TableField(value = "`hospital_address`")
    private String hospital_address;
    // 医院类型
    @TableField(value = "`type_of_hospital`")
    private String type_of_hospital;
    // 医院距离
    @TableField(value = "`distance_from_hospital`")
    private String distance_from_hospital;
    // 医院电话
    @TableField(value = "`hospital_phone_number`")
    private String hospital_phone_number;
    // 医院评分
    @TableField(value = "`hospital_score`")
    private String hospital_score;
    // 医院图片
    @TableField(value = "`hospital_pictures`")
    private String hospital_pictures;
    // 擅长领域
    @TableField(value = "`areas_of_expertise`")
    private String areas_of_expertise;
    // 医院介绍
    @TableField(value = "`hospital_introduction`")
    private String hospital_introduction;
    // 医院详情
    @TableField(value = "`hospital_details`")
    private String hospital_details;

    // 点击数
    @TableField(value = "hits")
    private Integer hits;

    // 点赞数
    @TableField(value = "praise_len")
    private Integer praise_len;

    // 收藏数
    @TableField(value = "collect_len")
    private Integer collect_len;

    // 评论数
    @TableField(value = "comment_len")
    private Integer comment_len;



















	// 更新时间
    @TableField(value = "update_time")
    private Timestamp update_time;

    // 创建时间
    @TableField(value = "create_time")
    private Timestamp create_time;

}
