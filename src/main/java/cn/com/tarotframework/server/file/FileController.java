package cn.com.tarotframework.server.file;

import cn.com.tarotframework.exception.OakException;
import cn.com.tarotframework.response.TarotResponseResultBody;
import cn.com.tarotframework.response.TarotResult;
import cn.com.tarotframework.utils.FileUtils;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;

@Api(tags = {"资料管理-工时上传"})
@Validated
@RestController
@RequestMapping("file")
@TarotResponseResultBody
public class FileController {

    @Value("${tarot.file.upload.path}")
    public String targetFilePath;

    @PostMapping("/upload")
    @ApiOperation("工时文件上传")
    public TarotResult<String> uploadFile(@RequestPart @RequestParam("file") MultipartFile file) {
        if(file.isEmpty()) {
            throw new OakException(6000, "上传文件不能为空");
        }
      String url =  FileUtils.saveFile(file, targetFilePath);
        return TarotResult.success(url);
    }

}
