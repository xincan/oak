package cn.com.tarotframework.server.oak.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @program: tarot-authorization-server
 * @description: 用户映射数据库表实体类
 * @author: Jiang Xincan
 * @version: 0.0.1
 * @create: 2022/5/20 18:54
 **/
@Data
@TableName("sys_user_role")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SysUserRole {

    @TableId(value = "role_id")
    private Long roleId;

    @TableField(value = "user_id")
    private Long userId;

}
