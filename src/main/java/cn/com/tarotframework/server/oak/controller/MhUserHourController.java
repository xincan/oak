package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Api(tags = {"系统管理-工时管理"})
@Validated
@RestController
@RequestMapping("hour")
@TarotResponseResultBody
public class MhUserHourController {

    private final IMhUserHourService mhUserHourService;

    public MhUserHourController(IMhUserHourService mhUserHourService) {
        this.mhUserHourService = mhUserHourService;
    }

    @ApiOperation(value = "3-添加项目工时信息", httpMethod = "POST", notes = "根据每个人所在的项目添加工时")
    @PostMapping("insert")
    public void inertProjectHour(@ApiParam(name = "file", value = "文件名称：格式（2021-汇总.xlsx  或  2021-08.xlsx）", required = true, example = "")
                                     @NotNull(message = "文件名称不能为空")
                                     @RequestParam(name = "file") String file) {

        mhUserHourService.insert(file);

    }
}
