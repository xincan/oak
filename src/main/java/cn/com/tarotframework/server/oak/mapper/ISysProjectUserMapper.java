package cn.com.tarotframework.server.oak.mapper;

import cn.com.tarotframework.server.oak.po.SysProjectUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program oak
 * @description 项目配置人员接口
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/9/28 18:54
 */
@Mapper
public interface ISysProjectUserMapper extends BaseMapper<SysProjectUser> {
}
