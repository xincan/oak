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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 工时填报表(MhUserHour)实体类
 *
 * @author makejava
 * @since 2021-09-13 14:06:55
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@TableName("mh_user_hour")
public class MhUserHour implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField(value = "user_id")
    private Long userId;

    @TableField(value = "fill_date")
    private LocalDate fillDate;

    @TableField(value = "total_hour")
    private BigDecimal totalHour;

    @TableField(value = "create_time")
    private LocalDateTime createTime;

}