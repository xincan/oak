package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.exception.OakException;
import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.utils.OsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@Api(tags = {"系统管理-工时管理"})
@Validated
@RestController
@RequestMapping("hour")
@TarotResponseResultBody
public class MhUserHourController {

    @Value("${tarot.file.upload.path}")
    public String targetFilePath;

    private final IMhUserHourService mhUserHourService;

    public MhUserHourController(IMhUserHourService mhUserHourService) {
        this.mhUserHourService = mhUserHourService;
    }

    @ApiOperation(value = "3-添加项目工时信息", httpMethod = "POST", notes = "根据每个人所在的项目添加工时")
    @PostMapping("insert")
    public void inertProjectHour(@ApiParam(value = "文件", required = true) @RequestPart @RequestParam("file") MultipartFile file) {
        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new OakException(6000, "上传文件不能为空");
        }

        mhUserHourService.insert(OsUtil.getOsPath() + targetFilePath + file.getOriginalFilename());

    }
}
