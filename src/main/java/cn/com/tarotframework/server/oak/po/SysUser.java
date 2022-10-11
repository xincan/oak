package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program: oak
 * @description: 用户映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/9/28 18:54
 **/
@Data
@TableName("sys_user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUser {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;

    @TableField(value = "dept_id")
    private Long deptId;

    @TableField(value = "user_name")
    private String userName;

    @TableField(value = "nick_name")
    private String nickName;

    @TableField(value = "user_type")
    private String userType;

    @TableField(value = "email")
    private String email;

    @TableField(value = "phonenumber")
    private int phoneNumber;

    @TableField(value = "sex")
    private String sex;

    @TableField(value = "avatar")
    private String avatar;

    @TableField(value = "password")
    private String password;

    @TableField(value = "status")
    private String status;

    @TableField(value = "del_flag")
    private String delFlag;

    @TableField(value = "login_ip")
    private String loginIp;

    @TableField(value = "login_date")
    private LocalDate loginDate;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(value = "remark", fill = FieldFill.INSERT)
    private String remark;

    @TableField(exist = false)
    private String departmentName;

    @TableField(exist = false)
    private Long sysUserRoleId;

    @TableField(exist = false)
    private Long sysUserPostId;

    @TableField(exist = false)
    List<SysProjectUser> projectUserList;

    @TableField(exist = false)
    List<SysProject> projects;

}
