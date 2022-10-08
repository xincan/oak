package cn.com.tarotframework.server.oak.mapper;

import cn.com.tarotframework.server.oak.po.MhHourDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
public interface IMhDataScourMapper extends BaseMapper<MhHourDetail> {

    List<MhHourDetail> getMhHourDetail(@Param("userId") Long userId, @Param("date") String date);

}
