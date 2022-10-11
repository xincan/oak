package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * copyright (C), 2022, 同创工时系统
 * 项目工时表
 * @program: oak
 * @description: 项目工时映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/9/28 18:54
 **/
@Data
@TableName("mh_project_hour")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MhProjectHour implements Serializable {

    @TableId(value = "project_id", type = IdType.AUTO)
    private Long projectId;

    @TableField(value = "man_hour")
    private BigDecimal manHour;

    @TableField(value = "use_hour")
    private BigDecimal useHour;


}
