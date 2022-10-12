package cn.com.tarotframework.server.oak.mapper;

import cn.com.tarotframework.server.oak.po.MhUserHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program oak
 * @description 用户当天工时
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/9/28 18:54
 */
@Mapper
public interface IMhUserHourMapper extends BaseMapper<MhUserHour> {

    void deleteMhUserHour(@Param("month") String month);

}
