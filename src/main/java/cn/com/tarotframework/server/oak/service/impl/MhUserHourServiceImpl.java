package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.dto.ProjectHourDetail;
import cn.com.tarotframework.server.oak.dto.User;
import cn.com.tarotframework.server.oak.mapper.*;
import cn.com.tarotframework.server.oak.po.*;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * copyright (C), 2022, 塔罗牌基础架构
 *
 * @author Jiang Xincan
 * @version 0.0.1
 * @program tarot-authorization-server
 * @description 用户信息接口实现类
 * @create 2022/5/20 18:54
 **/
@Service
public class MhUserHourServiceImpl implements IMhUserHourService {


    private final ISysUserMapper sysUserMapper;

    private final IMhUserHourMapper mhUserHourMapper;

    private final IMhHourDetailMapper mhHourDetailMapper;

    private final ISysProjectMapper sysProjectMapper;

    private final IMhProjectHourMapper mhProjectHourMapper;

    public MhUserHourServiceImpl(ISysUserMapper sysUserMapper, IMhUserHourMapper mhUserHourMapper, IMhHourDetailMapper mhHourDetailMapper,
                                 ISysProjectMapper sysProjectMapper, IMhProjectHourMapper mhProjectHourMapper) {
        this.sysUserMapper = sysUserMapper;
        this.mhUserHourMapper = mhUserHourMapper;
        this.mhHourDetailMapper = mhHourDetailMapper;
        this.sysProjectMapper = sysProjectMapper;
        this.mhProjectHourMapper = mhProjectHourMapper;
    }


    private List<User> selectExcelDataList(String filePath) {
        //获取excel全量数据
        List<ExcelData> excelDataLists = OakDataUtil.getExcelData(filePath);
        String year = filePath.substring(filePath.lastIndexOf("/") + 1).split("-")[0];
        // 获取全量数据
        return OakDataUtil.getProjectHours(excelDataLists, year);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(String file) {

        // 获取系统所有项目
        LambdaQueryWrapper<SysProject> sysProjectLambdaQueryWrapper = Wrappers.lambdaQuery();
        sysProjectLambdaQueryWrapper.select(SysProject::getProjectId, SysProject::getProjectName);
        List<SysProject> sysProjects = sysProjectMapper.selectList(sysProjectLambdaQueryWrapper);
        sysProjects.forEach(sp -> {
            LambdaQueryWrapper<MhProjectHour> mhProjectHourLambdaQueryWrapper = Wrappers.lambdaQuery(MhProjectHour.class)
                    .eq(MhProjectHour::getProjectId, sp.getProjectId())
                    .select(MhProjectHour::getProjectId, MhProjectHour::getManHour);
            MhProjectHour mhProjectHour = mhProjectHourMapper.selectOne(mhProjectHourLambdaQueryWrapper);
            sp.setDuration(mhProjectHour.getManHour().doubleValue());
        });

        // 获取全量数据
        List<User> userList = selectExcelDataList(file);

        // 遍历全量数据
        userList.forEach( user -> {

            // 获取用户ID
            LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getNickName, user.getUserName())
                    .select(SysUser::getUserId, SysUser::getNickName);
            SysUser sysUser = sysUserMapper.selectOne(userLambdaQueryWrapper);
            Long userId = sysUser.getUserId();

            // 遍历用户对应的项目工时
            user.getProjectHours().stream().filter(ph -> ph.getUserName().equals(user.getUserName())).forEach( ph -> {

                // 遍历用户对应每个项目每天的详细工时
                for (ProjectHourDetail phd : ph.getProjectHourDetails()) {
                    // 先根据用户ID查询，对应的每天项目总工时，
                    // 判断当天对应项目总工时ID是否存在，如果不存在则插入总工时数据，否则获取总工时Id
                    LambdaQueryWrapper<MhUserHour> mhUserHourLambdaQueryWrapper = Wrappers.lambdaQuery();
                    mhUserHourLambdaQueryWrapper.eq(MhUserHour::getUserId, userId)
                            .eq(MhUserHour::getFillDate, phd.getFillDate())
                            .select(MhUserHour::getId);
                    MhUserHour uh = mhUserHourMapper.selectOne(mhUserHourLambdaQueryWrapper);
                    if (uh == null) {
                        uh = new MhUserHour();
                        // 组装当前人，当前天，对应的项目总工时，且插入系统
                        MhUserHour mhUserHour = MhUserHour.builder()
                                .userId(userId).fillDate(phd.getFillDate())
                                .totalHour(BigDecimal.valueOf(ph.getTotalHour())).createTime(ph.getCreateTime()).build();
                        mhUserHourMapper.insert(mhUserHour);
                        uh.setId(mhUserHour.getId());
                    }

                    // 根据项目名称，比对当前用户对应填报的项目信息，填充项目ID
                    MhUserHour finalUh = uh;
                    sysProjects.stream().filter(p -> p.getProjectName().equals(phd.getProjectName())).forEach(p -> {
                        MhHourDetail mhHourDetail = MhHourDetail.builder()
                                .projectId(p.getProjectId()).userId(userId)
                                .hourId(finalUh.getId())
                                .useHour(BigDecimal.valueOf(phd.getUseHour())).fillDate(phd.getFillDate())
                                .projectStatus(phd.getProjectStatus()).everyday(phd.getEveryDay())
                                .daily(phd.getDaily()).createTime(phd.getCreateTime())
                                .build();
                        mhHourDetailMapper.insert(mhHourDetail);
                        System.out.println(userId + "    " + finalUh.getId() + "   " + p.getProjectId() + "   " + mhHourDetail.getId());
                    });
                }
            });
        });
    }
}
