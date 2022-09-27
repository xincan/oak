package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.dto.User;
import cn.com.tarotframework.server.oak.mapper.*;
import cn.com.tarotframework.server.oak.po.*;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectUserService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    private final ISysProjectUserMapper sysProjectUserMapper;

    private final ISysProjectMapper sysProjectMapper;

    private final IMhProjectHourMapper mhProjectHourMapper;


    public MhUserHourServiceImpl(ISysUserMapper sysUserMapper, IMhUserHourMapper mhUserHourMapper,
                                 ISysProjectUserMapper sysProjectUserMapper, IMhHourDetailMapper mhHourDetailMapper,
                                 ISysProjectMapper sysProjectMapper,
                                 IMhProjectHourMapper mhProjectHourMapper) {
        this.sysUserMapper = sysUserMapper;
        this.mhUserHourMapper = mhUserHourMapper;
        this.sysProjectUserMapper = sysProjectUserMapper;
        this.mhHourDetailMapper = mhHourDetailMapper;
        this.sysProjectMapper = sysProjectMapper;
        this.mhProjectHourMapper = mhProjectHourMapper;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void insert(String year) {

        // 获取全量数据
        List<User> userList = OakDataUtil.getProjectHours(year);


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


        userList.forEach( user -> {

            LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = Wrappers.lambdaQuery(SysUser.class)
                    .eq(SysUser::getNickName, user.getUserName())
                    .select(SysUser::getUserId, SysUser::getNickName);
            SysUser sysUser = sysUserMapper.selectOne(userLambdaQueryWrapper);
            Long userId = sysUser.getUserId();

            user.getProjectHours().stream().filter(ph -> ph.getUserName().equals(user.getUserName())).forEach( ph -> {

                // 组装当前人，当前天，对应的项目总工时，且插入系统
                MhUserHour mhUserHour = MhUserHour.builder()
                        .userId(userId).fillDate(ph.getFillDate())
                        .totalHour(BigDecimal.valueOf(ph.getTotalHour())).createTime(ph.getCreateTime()).build();

                mhUserHourMapper.insert(mhUserHour);

                ///////////////////////////////////////////////////////////////////////////////////////////////////////

                ph.getProjectHourDetails().forEach( phd -> {

                    // 根据项目名称，比对当前用户对应填报的项目信息，填充项目ID
                    sysProjects.stream().filter( p -> p.getProjectName().equals(phd.getProjectName())).forEach( p -> {
                        MhHourDetail mhHourDetail =MhHourDetail.builder()
                                .projectId(p.getProjectId()).userId(userId)
                                .hourId(mhUserHour.getId())
                                .useHour(phd.getUseHour()).fillDate(phd.getFillDate())
                                .projectStatus(phd.getProjectStatus()).everyday(phd.getEveryDay())
                                .daily(phd.getDaily()).createTime(phd.getCreateTime())
                                .build();
                        mhHourDetailMapper.insert(mhHourDetail);
                        System.out.println(userId + "    " + mhUserHour.getId() + "   " + p.getProjectId() + "   " + mhHourDetail.getId());
                    });
                });

            });
        });
    }


}
