package cn.com.tarotframework.server.oak.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHourDetail {

    private String month;

    private Long projectId;

    private String projectName;

    private Long userId;

    private String userName;

    // 填报日期
    private LocalDate fillDate;

    // 使用工时
    private BigDecimal useHour;

    // 项目状态：a 进行中，b运维，c 结束
    private String projectStatus;

    private Long mhUserHourId;

    // 是否为每日上报工时记录 0 否  1 是
    private int everyDay;

    // 日报内容
    private String daily;

    private LocalDateTime createTime;

    private Integer count;

    private Double sum;

}
