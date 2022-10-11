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
 * @description: 用户映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/9/28 18:54
 **/
@Data
@TableName("sys_user_post")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserPost {

    @TableId(value = "post_id")
    private Long postId;

    @TableField(value = "user_id")
    private Long userId;

}
