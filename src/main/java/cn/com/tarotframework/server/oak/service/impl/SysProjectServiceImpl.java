package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.dto.User;
import cn.com.tarotframework.server.oak.mapper.IMhProjectHourMapper;
import cn.com.tarotframework.server.oak.mapper.ISysProjectMapper;
import cn.com.tarotframework.server.oak.po.MhProjectHour;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysUser;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

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

    private List<SysProject> selectExcelDataList(String filePath) {
        //获取excel全量数据
        List<ExcelData> excelDataLists = OakDataUtil.getExcelData(filePath);
        String year = filePath.substring(filePath.lastIndexOf("/") + 1).split("-")[0];
        // 获取全量数据
        return OakDataUtil.getProjects(excelDataLists, year);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(String filePath) {

        List<SysProject> projects = selectExcelDataList(filePath);
        // 获取项目信息
        LambdaQueryWrapper<SysProject> projectLambdaQueryWrapper = Wrappers.lambdaQuery();
        projectLambdaQueryWrapper.select(SysProject::getProjectId, SysProject::getProjectName);
        List<SysProject> sysProjects = this.sysProjectMapper.selectList(projectLambdaQueryWrapper);

        // 求users中，sysUsers的补集
        List<SysProject> insertProjects = projects.stream().filter(project ->
                        !sysProjects.stream().map(SysProject::getProjectName)
                                .collect(Collectors.toList()).contains(project.getProjectName()))
                .collect(Collectors.toList());

        insertProjects.forEach( project -> {
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
