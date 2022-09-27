package cn.com.tarotframework.server.oak.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    private Double totalHour;

    // 填报日期
    private LocalDate fillDate;

    private LocalDateTime createTime;

    List<ProjectHourDetail> projectHourDetails;

}
