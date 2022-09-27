package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


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

    @ApiOperation(value = "添加项目工时", httpMethod = "POST", notes = "根据每个人所在的项目添加工时")
    @PostMapping("insert")
    public void inertProjectHour(String year) {

        mhUserHourService.insert(year);

    }
}
