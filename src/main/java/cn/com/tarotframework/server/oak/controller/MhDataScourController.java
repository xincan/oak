package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.exception.OakException;
import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.server.oak.service.IMhDataScourService;
import cn.com.tarotframework.utils.OsUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"数据冲洗"})
@Validated
@RestController
@RequestMapping("scour")
@TarotResponseResultBody
public class MhDataScourController {


    private final IMhDataScourService mhDataScourService;

    public MhDataScourController(IMhDataScourService mhDataScourService) {
        this.mhDataScourService = mhDataScourService;
    }

    @ApiOperation(value = "4-数据冲洗", httpMethod = "POST", notes = "清洗当天总工时之和小于等于当天各个项目工时总和")
    @PostMapping
    public void scour(@ApiParam(value = "文件", required = true) @RequestPart @RequestParam("file") MultipartFile file) {
        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new OakException(6000, "上传文件不能为空");
        }

        mhDataScourService.dataScour(file.getOriginalFilename());
    }

}
