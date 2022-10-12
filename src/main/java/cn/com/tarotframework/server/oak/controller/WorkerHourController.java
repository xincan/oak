package cn.com.tarotframework.server.oak.controller;

import cn.com.tarotframework.exception.OakException;
import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.response.TarotResult;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.server.oak.service.ISysUserService;
import cn.com.tarotframework.server.oak.service.IWorkerHourService;
import cn.com.tarotframework.utils.FileUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"0-工时导入"})
@Validated
@RestController
@RequestMapping("file")
@TarotResponseResultBody
public class WorkerHourController {

    @Value("${tarot.file.upload.path}")
    public String targetFilePath;

    private final ISysUserService sysUserService;

    private final ISysProjectService sysProjectService;

    private final IMhUserHourService mhUserHourService;

    private IWorkerHourService workerHourService;

    public WorkerHourController(ISysUserService sysUserService, ISysProjectService sysProjectService, IMhUserHourService mhUserHourService, IWorkerHourService workerHourService) {
        this.sysUserService = sysUserService;
        this.sysProjectService = sysProjectService;
        this.mhUserHourService = mhUserHourService;
        this.workerHourService = workerHourService;
    }


    @PostMapping("/upload")
    @ApiOperation("1：全量数据导入，增量数据导入")
    public TarotResult<String> uploadFileData(@ApiParam(value = "上传文件", required = true) @RequestPart @RequestParam("file") MultipartFile file) {
        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new OakException(6000, "上传文件不能为空");
        }
        String filePath =  FileUtils.saveFile(file, targetFilePath);
        if(StringUtils.hasLength(filePath)){
            workerHourService.uploadFileData(filePath);
            return TarotResult.success(filePath);
        }
        return TarotResult.error("导入失败：" + file.getOriginalFilename());
    }

    @PostMapping("/update")
    @ApiOperation("2：数据修订导入")
    public TarotResult<String> updateFileData(@ApiParam(value = "上传文件", required = true) @RequestPart @RequestParam("file") MultipartFile file) {
        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new OakException(6000, "上传文件不能为空");
        }
        String filePath =  FileUtils.saveFile(file, targetFilePath);
        if(StringUtils.hasLength(filePath)){
            workerHourService.updateFileData(filePath);
            return TarotResult.success(filePath);
        }
        return TarotResult.error("导入失败：" + file.getOriginalFilename());
    }

}
