package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.mapper.IMhProjectHourMapper;
import cn.com.tarotframework.server.oak.mapper.ISysProjectMapper;
import cn.com.tarotframework.server.oak.po.MhProjectHour;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program oak
 * @description 项目信息接口实现类
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/9/28 18:54
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

    @Override
    public void insert(String filePath) {

        List<SysProject> projects = selectExcelDataList(filePath);
        // 获取目前数据库存在的项目信息
        LambdaQueryWrapper<SysProject> projectLambdaQueryWrapper = Wrappers.lambdaQuery();
        projectLambdaQueryWrapper.select(SysProject::getProjectId, SysProject::getProjectName);
        List<SysProject> sysProjects = this.sysProjectMapper.selectList(projectLambdaQueryWrapper);

        // 如果数据库有项目信息，则插入补集
        // 否则插入全部
        if (CollectionUtils.isEmpty(sysProjects)) {
            handleProject(projects);
        } else {
            // 求projects中，sysProjects的补集，将补集数据插入数据库
            List<SysProject> insertProjects = projects.stream().filter(project ->
                            !sysProjects.stream().map(SysProject::getProjectName)
                                    .collect(Collectors.toList()).contains(project.getProjectName()))
                    .collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(insertProjects)) {
                handleProject(insertProjects);
            }
            // 求projects，sysProjects的交集，叠加累计总工时，更新入库mh_project_hour
            projects.forEach( project -> {
                sysProjects.forEach( sp -> {
                    if (project.getProjectName().equals(sp.getProjectName())) {
                        // 根据projectId, 查询对应的MhProjectHour
                        MhProjectHour mhProjectHour = mhProjectHourMapper.selectOne(Wrappers.lambdaQuery(MhProjectHour.class)
                                .eq(MhProjectHour::getProjectId, sp.getProjectId())
                                .select(MhProjectHour::getManHour));
                        // 叠加总工时，然后更新
                        mhProjectHourMapper.update(
                                MhProjectHour.builder().manHour(mhProjectHour.getManHour().add(BigDecimal.valueOf(project.getDuration()))).build(),
                                Wrappers.lambdaQuery(MhProjectHour.class).eq(MhProjectHour::getProjectId, sp.getProjectId())
                        );
                    }
                });
            });
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleProject(List<SysProject> projects) {
        projects.forEach( project -> {
            sysProjectMapper.insert(project);
            mhProjectHourMapper.insert(MhProjectHour.builder()
                    .projectId(project.getProjectId())
                    .manHour(BigDecimal.valueOf(project.getDuration()))
                    .useHour(BigDecimal.valueOf(project.getDuration()))
                    .build());
        });
    }

}
