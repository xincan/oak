package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program: oak
 * @description: 部门映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/9/28 18:54
 **/
@Data
@TableName("sys_dept")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysDept {

    @TableId(value = "dept_id", type = IdType.AUTO)
    private Long deptId;

    @TableField(value = "dept_name")
    private String deptName;

    @TableField(value = "parent_id")
    private Long parentId;

    @TableField(value = "ancestors")
    private String ancestors;

    @TableField(value = "order_num")
    private int orderNum;

    @TableField(value = "leader")
    private String leader;

    @TableField(value = "phone")
    private String phone;

    @TableField(value = "email")
    private String email;


    @TableField(value = "status")
    private String status;

    @TableField(value = "del_flag")
    private String delFlag;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
