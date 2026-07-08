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
 * 知识科普：(KnowledgePopularization)表实体类
 *
 */
@TableName("`knowledge_popularization`")
@Data
@EqualsAndHashCode(callSuper = false)
public class KnowledgePopularization implements Serializable {

    // KnowledgePopularization编号
    @TableId(value = "knowledge_popularization_id", type = IdType.AUTO)
    private Integer knowledge_popularization_id;

    // 皮肤病名称
    @TableField(value = "`name_of_skin_disease`")
    private String name_of_skin_disease;
    // 皮肤病种类
    @TableField(value = "`types_of_skin_diseases`")
    private String types_of_skin_diseases;
    // 发布日期
    @TableField(value = "`release_date`")
    private Timestamp release_date;
    // 治疗方式
    @TableField(value = "`treatment_mode`")
    private String treatment_mode;
    // 科普图片
    @TableField(value = "`popular_science_pictures`")
    private String popular_science_pictures;
    // 科普视频
    @TableField(value = "`popular_science_video`")
    private String popular_science_video;
    // 皮肤病介绍
    @TableField(value = "`skin_disease_introduction`")
    private String skin_disease_introduction;
    // 治疗方案
    @TableField(value = "`treatment_plan`")
    private String treatment_plan;
    // 科普详情
    @TableField(value = "`science_popularization_details`")
    private String science_popularization_details;

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
