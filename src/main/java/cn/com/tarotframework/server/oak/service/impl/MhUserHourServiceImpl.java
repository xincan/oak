package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.dto.ExcelData;
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
 * copyright (C), 2022, 同创工时系统
 *
 * @author Jiang Xincan
 * @version 0.0.1
 * @program oak
 * @description 工时详情处理类
 * @create 2022/9/28 18:54
 **/
@Service
public class MhUserHourServiceImpl implements IMhUserHourService {


    private final ISysUserMapper sysUserMapper;

    private final IMhUserHourMapper mhUserHourMapper;

    private final IMhHourDetailMapper mhHourDetailMapper;

    private final IMhProjectHourMapper mhProjectHourMapper;

    private final ISysProjectMapper sysProjectMapper;


    public MhUserHourServiceImpl(ISysUserMapper sysUserMapper, IMhUserHourMapper mhUserHourMapper, IMhHourDetailMapper mhHourDetailMapper, IMhProjectHourMapper mhProjectHourMapper, ISysProjectMapper sysProjectMapper) {
        this.sysUserMapper = sysUserMapper;
        this.mhUserHourMapper = mhUserHourMapper;
        this.mhHourDetailMapper = mhHourDetailMapper;
        this.mhProjectHourMapper = mhProjectHourMapper;
        this.sysProjectMapper = sysProjectMapper;
    }


    private List<User> selectExcelDataList(String filePath) {
        //获取excel全量数据
        List<ExcelData> excelDataLists = OakDataUtil.getExcelData(filePath);
        // 获取全量数据
        return OakDataUtil.getProjectHours(excelDataLists);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(String file) {

        // 获取系统所有项目
        LambdaQueryWrapper<SysProject> sysProjectLambdaQueryWrapper = Wrappers.lambdaQuery();
        sysProjectLambdaQueryWrapper.select(SysProject::getProjectId, SysProject::getProjectName);
        List<SysProject> sysProjects = sysProjectMapper.selectList(sysProjectLambdaQueryWrapper);

        // 获取全量数据
        List<User> userList = selectExcelDataList(file);

        // 遍历全量数据
        userList.forEach( user -> {
            // 获取用户ID
            LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getUserName, user.getUserName())
                    .select(SysUser::getUserId, SysUser::getUserName);
            SysUser sysUser = sysUserMapper.selectOne(userLambdaQueryWrapper);
            Long userId = sysUser.getUserId();

            user.getProjectHours().forEach( ph -> {

                // 插入工时填报表，mh_user_hour, 获取 hour_id, 插入工时填报详情表 mh_hour_detail 中
                MhUserHour mhUserHour = MhUserHour.builder().userId(userId).fillDate(ph.getFillDate())
                        .totalHour(ph.getTotalHour()).createTime(ph.getCreateTime()).build();
                mhUserHourMapper.insert(mhUserHour);

                // 插入工时填报详情表 mh_hour_detail
                ph.getProjectHourDetails().forEach( phd -> {
                    sysProjects.stream().filter(sp -> sp.getProjectName().equals(phd.getProjectName())).forEach( sp -> phd.setProjectId(sp.getProjectId()));
                    mhHourDetailMapper.insert(MhHourDetail.builder()
                        .projectId(phd.getProjectId()).userId(userId)
                        .hourId(mhUserHour.getId())
                        .useHour(BigDecimal.valueOf(phd.getUseHour())).fillDate(phd.getFillDate())
                        .projectStatus(phd.getProjectStatus()).everyday(phd.getEveryDay())
                        .daily(phd.getDaily()).createTime(phd.getCreateTime())
                        .build());

                    // 更新项目使用工时 mh_project_hour 中的 use_hour
                    // 获取之前项目上使用工时
                    MhProjectHour mhProjectHour = mhProjectHourMapper.selectOne(
                            Wrappers.lambdaQuery(MhProjectHour.class).eq(MhProjectHour::getProjectId, phd.getProjectId())
                    );
                    BigDecimal useHour = mhProjectHour.getUseHour();
                    mhProjectHourMapper.update(
                            MhProjectHour.builder().useHour(useHour.add(BigDecimal.valueOf(phd.getUseHour()))).build(),
                            Wrappers.lambdaQuery(MhProjectHour.class).eq(MhProjectHour::getProjectId, phd.getProjectId())
                    );
                });
            });
        });
    }
}
