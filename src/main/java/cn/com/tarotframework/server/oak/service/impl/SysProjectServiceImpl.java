package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.mapper.IMhProjectHourMapper;
import cn.com.tarotframework.server.oak.mapper.ISysProjectMapper;
import cn.com.tarotframework.server.oak.po.MhProjectHour;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @program tarot-authorization-server
 * @description 用户信息接口实现类
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/5/20 18:54
 **/
@Service
public class SysProjectServiceImpl implements ISysProjectService {

    private final ISysProjectMapper sysProjectMapper;

    private final IMhProjectHourMapper mhProjectHourMapper;

    public SysProjectServiceImpl(ISysProjectMapper sysProjectMapper, IMhProjectHourMapper mhProjectHourMapper) {
        this.sysProjectMapper = sysProjectMapper;
        this.mhProjectHourMapper = mhProjectHourMapper;
    }

    @Override
    public void insert(List<SysProject> projects) {

        projects.forEach( project -> {
            sysProjectMapper.insert(project);
            MhProjectHour mhProjectHour = MhProjectHour.builder()
                    .projectId(project.getProjectId())
                    .manHour(BigDecimal.valueOf(project.getDuration()))
                    .useHour(BigDecimal.valueOf(project.getDuration()))
                    .build();
            mhProjectHourMapper.insert(mhProjectHour);
        });

    }


}
