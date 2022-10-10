package cn.com.tarotframework.server.oak.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProjectHour {

    private Long userId;

    private String userName;

    private Long projectId;

    private String projectName;

    //当前天参与所有项目总工时
    private BigDecimal totalHour;

    // 填报日期
    private LocalDate fillDate;

    // 创建日期
    private LocalDateTime createTime;

    List<ProjectHourDetail> projectHourDetails;

}
