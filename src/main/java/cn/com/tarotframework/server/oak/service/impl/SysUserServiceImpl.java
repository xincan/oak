package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.mapper.*;
import cn.com.tarotframework.server.oak.po.*;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.server.oak.service.ISysUserService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class SysUserServiceImpl implements ISysUserService {

    private final ISysUserMapper sysUserMapper;

    private final ISysDeptMapper sysDeptMapper;

    private final ISysUserPostMapper sysUserPostMapper;

    private final ISysUserRoleMapper sysUserRoleMapper;

    private final ISysProjectMapper sysProjectMapper;

    private final ISysProjectUserMapper sysProjectUserMapper;


    public SysUserServiceImpl(ISysUserMapper sysUserMapper, ISysDeptMapper sysDeptMapper, ISysUserPostMapper sysUserPostMapper, ISysUserRoleMapper sysUserRoleMapper, ISysProjectMapper sysProjectMapper, ISysProjectUserMapper sysProjectUserMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysDeptMapper = sysDeptMapper;
        this.sysUserPostMapper = sysUserPostMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysProjectMapper = sysProjectMapper;
        this.sysProjectUserMapper = sysProjectUserMapper;
    }

    @Override
    public void insert(String year) {
        List<SysUser> users = OakDataUtil.getUsers(year);

        // 获取项目信息
        LambdaQueryWrapper<SysProject> projectLambdaQueryWrapper = Wrappers.lambdaQuery();
        projectLambdaQueryWrapper.select(SysProject::getProjectId, SysProject::getProjectName);
        List<SysProject> projects = this.sysProjectMapper.selectList(projectLambdaQueryWrapper);

        // 比对用户与部门集合，将比对上的部门信息ID，回填给用户对象
        users.forEach( user -> {
            projects.forEach( project -> {
                user.getProjectUserList().stream()
                        .filter( sysProjectUser -> project.getProjectName().equals(sysProjectUser.getProjectName()) )
                        .forEach( sysProjectUser -> sysProjectUser.setProjectId(project.getProjectId()) );
            });
        });
        users.forEach( user -> {
            sysUserMapper.insert(user);
            sysUserPostMapper.insert(SysUserPost.builder().userId(user.getUserId()).postId(user.getSysUserPostId()).build());
            sysUserRoleMapper.insert(SysUserRole.builder().userId(user.getUserId()).roleId(user.getSysUserRoleId()).build());
            user.getProjectUserList().forEach( project -> {
                project.setUserId(user.getUserId());
                sysProjectUserMapper.insert(project);
            });
        });
    }
}
