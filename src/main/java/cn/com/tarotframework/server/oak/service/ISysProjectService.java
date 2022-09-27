package cn.com.tarotframework.server.oak.service;

import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysUser;
import cn.com.tarotframework.server.oak.vo.UserSearchVo;

import java.util.List;


public interface ISysProjectService {

    void insert(List<SysProject> projects);

}
