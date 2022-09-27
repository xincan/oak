package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @program: tarot-authorization-server
 * @description: 用户映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/5/20 18:54
 **/
@Data
@TableName("sys_project_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysProjectUser {

    @TableId(value = "project_id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "project_id")
    private Long projectId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "status")
    private int status;

    @TableField(value = "remove_time")
    private LocalDateTime removeTime;

    @TableField(value = "everyday")
    private int everyday;

    @TableField(value = "create_user", fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_user", fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String projectName;

}
