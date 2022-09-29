package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.exception.OakException;
import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysUser;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.server.oak.service.ISysUserService;
import cn.com.tarotframework.utils.ConvertUtil;
import cn.com.tarotframework.utils.OakDataUtil;
import cn.com.tarotframework.utils.OsUtil;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Api(tags = {"系统管理-用户管理"})
@Validated
@RestController
@RequestMapping("user")
@TarotResponseResultBody
public class SysUserController {

    @Value("${tarot.file.upload.path}")
    public String targetFilePath;

    private final ISysUserService sysUserService;

    public SysUserController(ISysUserService sysUserService) {
        this.sysUserService = sysUserService;
    }


    @ApiOperation(value = "2-添加人员信息", httpMethod = "POST", notes = "添加人员信息、人员配置角色、人员配置部门、人员配置岗位、人员关联项目")
    @PostMapping("/user/insert")
    public void inertUsers(@ApiParam(value = "文件", required = true) @RequestPart @RequestParam("file") MultipartFile file) {

        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new OakException(6000, "上传文件不能为空");
        }

        sysUserService.insert(OsUtil.getOsPath() + targetFilePath + "/" + file.getOriginalFilename());
    }
}
