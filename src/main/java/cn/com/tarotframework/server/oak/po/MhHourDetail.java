package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * copyright (C), 2022, 同创工时系统
 * 工时填表详情表
 * @program: oak
 * @description: 工时填表详情映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/9/28 18:54
 **/
@Data
@TableName("mh_hour_detail")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MhHourDetail {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "hour_id")
    private Long hourId;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "project_id")
    private Long projectId;

    @TableField(value = "use_hour")
    private BigDecimal useHour;

    @TableField(value = "fill_date")
    private LocalDate fillDate;

    @TableField(value = "project_status")
    private String projectStatus;

    @TableField(value = "everyday")
    private int everyday;

    @TableField(value = "daily")
    private String daily;

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
