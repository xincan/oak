package cn.com.tarotframework.server.oak.service.impl;


import cn.com.tarotframework.server.oak.dto.ExcelData;
import cn.com.tarotframework.server.oak.mapper.*;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysUser;
import cn.com.tarotframework.server.oak.po.SysUserPost;
import cn.com.tarotframework.server.oak.po.SysUserRole;
import cn.com.tarotframework.server.oak.service.ISysUserService;
import cn.com.tarotframework.utils.OakDataUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * copyright (C), 2022, 同创工时系统
 *
 * @program oak
 * @description 用户信息接口实现类
 * @author Jiang Xincan
 * @version 0.0.1
 * @create 2022/9/28 18:54
 **/
@Service
public class SysUserServiceImpl implements ISysUserService {

    private final ISysUserMapper sysUserMapper;

    private final ISysUserPostMapper sysUserPostMapper;

    private final ISysUserRoleMapper sysUserRoleMapper;

    private final ISysProjectMapper sysProjectMapper;

    private final ISysProjectUserMapper sysProjectUserMapper;


    public SysUserServiceImpl(ISysUserMapper sysUserMapper, ISysUserPostMapper sysUserPostMapper,
                              ISysUserRoleMapper sysUserRoleMapper, ISysProjectMapper sysProjectMapper, ISysProjectUserMapper sysProjectUserMapper) {
        this.sysUserMapper = sysUserMapper;
        this.sysUserPostMapper = sysUserPostMapper;
        this.sysUserRoleMapper = sysUserRoleMapper;
        this.sysProjectMapper = sysProjectMapper;
        this.sysProjectUserMapper = sysProjectUserMapper;
    }

    private List<SysUser> selectExcelDataList(String filePath) {
        //获取excel全量数据
        List<ExcelData> excelDataLists = OakDataUtil.getExcelData(filePath);
        String year = OakDataUtil.getMonth(filePath).substring(0,4);
        // 获取全量数据
        return OakDataUtil.getUsers(excelDataLists, year);
    }

    @Override
    public void insert(String filePath) {

        List<SysUser> users = selectExcelDataList(filePath);

        // 获取项目信息(projectId, projectName)
        LambdaQueryWrapper<SysProject> projectLambdaQueryWrapper = Wrappers.lambdaQuery();
        projectLambdaQueryWrapper.select(SysProject::getProjectId, SysProject::getProjectName);
        List<SysProject> projects = this.sysProjectMapper.selectList(projectLambdaQueryWrapper);

        // 获取用户信息（userId, userName）
        LambdaQueryWrapper<SysUser> userLambdaQueryWrapper = Wrappers.lambdaQuery();
        userLambdaQueryWrapper.select(SysUser::getUserId, SysUser::getUserName);
        List<SysUser> sysUsers = this.sysUserMapper.selectList(userLambdaQueryWrapper);

        if (CollectionUtils.isEmpty(sysUsers)) {
            handleUser(projects, users);
        }else {
            // 求users中，sysUsers的补集
            List<SysUser> insertUsers = users.stream()
                    .filter(user ->
                            !sysUsers.stream().map(SysUser::getUserName).collect(Collectors.toList()).contains(user.getUserName())
                    ).collect(Collectors.toList());
            // 比对用户与部门集合，将比对上的部门信息ID，回填给用户对象
            if (!CollectionUtils.isEmpty(insertUsers)) {
                // 补充projectId 到 SysProjectUser 对象中
                handleUser(projects, insertUsers);
            }
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void handleUser(List<SysProject> projects, List<SysUser> insertUsers) {
        insertUsers.forEach( user -> {
            // 补充 SysProjectUser 对象中的projectId
            projects.forEach( project -> {
                user.getProjectUserList().stream()
                        .filter( sysProjectUser -> project.getProjectName().equals(sysProjectUser.getProjectName()) )
                        .forEach( sysProjectUser -> sysProjectUser.setProjectId(project.getProjectId()) );
            });

            // 根据用户名称，查询用户是否存在，如果存在，则登录名称加1， 一般判断汉字转拼音之后，有重复的情况
//            SysUser sysUser = sysUserMapper.selectOne(Wrappers.lambdaQuery(SysUser.class).eq(SysUser::getUserName, user.getUserName()));
//            if (!ObjectUtils.isEmpty(sysUser)) {
//                user.setUserName(sysUser.getUserName() + 1);
//            }
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
