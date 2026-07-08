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
 * 在线咨询：(OnlineConsultation)表实体类
 *
 */
@TableName("`online_consultation`")
@Data
@EqualsAndHashCode(callSuper = false)
public class OnlineConsultation implements Serializable {

    // OnlineConsultation编号
    @TableId(value = "online_consultation_id", type = IdType.AUTO)
    private Integer online_consultation_id;

    // 注册用户
    @TableField(value = "`registered_user`")
    private Integer registered_user;
    // 用户姓名
    @TableField(value = "`user_name`")
    private String user_name;
    // 咨询标题
    @TableField(value = "`advisory_title`")
    private String advisory_title;
    // 咨询类别
    @TableField(value = "`consulting_category`")
    private String consulting_category;
    // 咨询内容
    @TableField(value = "`consulting_content`")
    private String consulting_content;
    // 回复内容
    @TableField(value = "`reply_content`")
    private String reply_content;























	// 更新时间
    @TableField(value = "update_time")
    private Timestamp update_time;

    // 创建时间
    @TableField(value = "create_time")
    private Timestamp create_time;

}
