package cn.com.tarotframework.utils;

import cn.com.tarotframework.exception.OakException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileUtils {

    public static String saveFile(MultipartFile file, String targetFilePath) {
        //获取上传文件的完整名称,包括后缀名
        String filename = file.getOriginalFilename();

        //最终存放的目录   (contextPath/datePath)
        String os = OsUtil.getOsInfo();
        File targetFile = new File( os.equals("window") ? "c:" + targetFilePath : targetFilePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }

        //最终存放的目录及文件名
        File newTargetFile = new File(targetFile, filename);

        try {
            file.transferTo(newTargetFile);
            return newTargetFile.getPath() + "/" + filename;
        } catch (IOException e) {
            e.printStackTrace();
            throw new OakException(20001, "文件上传失败: " + filename);
        }
    }


    public static void deleteFile(String filename) {
        try {
            File file = new File(ResourceUtils.getURL("").getPath() + "/src/main/resources/static/" + filename);
            if (!file.isFile()) {
                System.out.println("删除失败！找不到指定文件");
                return;
            }

            if (file.delete()) {
                System.out.println("删除成功！");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {

    }


}
