package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program: oak
 * @description: 项目映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/9/28 18:54
 **/
@Data
@TableName("sys_project")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysProject {

    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    @TableField(value = "project_name")
    private String projectName;

    @TableField(value = "project_code")
    private String projectCode;

    @TableField(value = "project_manager")
    private Integer projectManager;

    @TableField(value = "project_status")
    private String projectStatus;

    @TableField(value = "enable")
    private int enable;

    @TableField(value = "deleted")
    private int deleted;

    @TableField(value = "start_date", fill = FieldFill.INSERT)
    private LocalDate startDate;

    @TableField(value = "end_date", fill = FieldFill.INSERT)
    private LocalDate endDate;

    @TableField(value = "remark", fill = FieldFill.INSERT)
    private String remark;

    @TableField(value = "create_by", fill = FieldFill.INSERT)
    private String createBy;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_by", fill = FieldFill.INSERT_UPDATE)
    private String updateBy;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 预计工时
    @TableField(exist = false)
    private Double duration;
}
