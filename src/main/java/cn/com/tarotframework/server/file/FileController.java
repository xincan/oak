package cn.com.tarotframework.server.file;

import cn.com.tarotframework.exception.OakException;
import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.response.TarotResult;
import cn.com.tarotframework.server.oak.service.IMhUserHourService;
import cn.com.tarotframework.server.oak.service.ISysProjectService;
import cn.com.tarotframework.server.oak.service.ISysUserService;
import cn.com.tarotframework.utils.FileUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = {"0-资料管理-工时上传"})
@Validated
@RestController
@RequestMapping("file")
@TarotResponseResultBody
public class FileController {

    @Value("${tarot.file.upload.path}")
    public String targetFilePath;

    private final ISysUserService sysUserService;

    private final ISysProjectService sysProjectService;

    private final IMhUserHourService mhUserHourService;

    public FileController(ISysUserService sysUserService, ISysProjectService sysProjectService, IMhUserHourService mhUserHourService) {
        this.sysUserService = sysUserService;
        this.sysProjectService = sysProjectService;
        this.mhUserHourService = mhUserHourService;
    }


    @PostMapping("/upload")
    @ApiOperation("工时文件上传")
    public TarotResult<String> uploadFile(@ApiParam(value = "文件", required = true) @RequestPart @RequestParam("file") MultipartFile file) {
        if(ObjectUtils.isEmpty(file) || file.getSize() <= 0) {
            throw new OakException(6000, "上传文件不能为空");
        }
        String filePath =  FileUtils.saveFile(file, targetFilePath);
        if(!StringUtils.isEmpty(filePath)){
            sysProjectService.insert(filePath);
            sysUserService.insert(filePath);
            mhUserHourService.insert(filePath);
            return TarotResult.success(filePath);
        }
        return TarotResult.error("导入失败：" + file.getOriginalFilename());
    }

}
