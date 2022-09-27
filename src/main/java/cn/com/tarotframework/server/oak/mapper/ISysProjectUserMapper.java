package cn.com.tarotframework.server.oak.mapper;

import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysProjectUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @program tarot-authorization-server
 * @description 用户数据处理接口
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/5/20 18:54
 */
@Mapper
public interface ISysProjectUserMapper extends BaseMapper<SysProjectUser> {
}
