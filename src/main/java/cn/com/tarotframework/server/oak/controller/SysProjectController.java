package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.server.oak.po.SysProject;
import cn.com.tarotframework.server.oak.po.SysUser;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.utils.ConvertUtil;
import cn.com.tarotframework.utils.EasyExcelUtil;
import cn.com.tarotframework.utils.OakDataUtil;
import com.alibaba.fastjson2.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Api(tags = {"管理-项目管理"})
@Validated
@RestController
@RequestMapping("project")
@TarotResponseResultBody
public class SysProjectController {

    private final ISysProjectService sysProjectService;

    public SysProjectController(ISysProjectService sysProjectService) {
        this.sysProjectService = sysProjectService;
    }

    @ApiOperation(value = "1-添加项目信息", httpMethod = "POST", notes = "批量添加项目信息")
    @PostMapping("insert")
    public void inertProject(@ApiParam(name = "file", value = "文件名称：格式（2021-汇总.xlsx  或  2021-08.xlsx）", required = true, example = "")
                                 @NotNull(message = "文件名称不能为空")
                                 @RequestParam(name = "file") String file) {
       sysProjectService.insert(file);
    }
}
