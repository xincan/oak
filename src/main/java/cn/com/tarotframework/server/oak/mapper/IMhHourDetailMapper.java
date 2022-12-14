package cn.com.tarotframework.server.oak.mapper;

import cn.com.tarotframework.server.oak.po.MhHourDetail;
import cn.com.tarotframework.server.oak.po.MhUserHour;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program oak
 * @description 工时详情
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/9/28 18:54
 */
@Mapper
public interface IMhHourDetailMapper extends BaseMapper<MhHourDetail> {

    List<MhHourDetail> getMhHourDetail(@Param("projectId") String projectId, @Param("userId") String userId, @Param("month") String month);

    void deleteMhHourDetail(@Param("projectId") String projectId, @Param("userId") String userId, @Param("month") String month);

}
