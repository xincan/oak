package cn.com.tarotframework.server.oak.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class User {


    private Long userId;

    private String userName;

    private String nickName;

    private String fillDate;

    // 创建时间
    private LocalDate createTime;

    List<ProjectHour> projectHours;



}
