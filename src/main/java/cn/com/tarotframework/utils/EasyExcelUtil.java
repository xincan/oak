package cn.com.tarotframework.utils;


import cn.com.tarotframework.server.oak.dto.ExcelData;
import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.BaseRowModel;
import com.alibaba.excel.metadata.Sheet;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/**
 * @author Excel数据处理类
 * @date 2022-05-18
 * @since v1.0.0
 */

public class EasyExcelUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(EasyExcelUtil.class);

    /**
     * 后期excel数据
     * @param filePath 文件路径
     * @param clazz ExcelData
     * @return Map<String, List<ExcelData>>
     */
    public static Map<String, List<ExcelData>> readExcelByData(String filePath, Class<ExcelData> clazz) {
        return readExcelByData(filePath, null, clazz);
    }


    public static Map<String, List<ExcelData>> readExcelByData(String filePath, String sheetName, Class<ExcelData> clazz) {
        Map<String, List<ExcelData>> dataListMap;
        InputStream inputStream = null;
        try {
            // 创建文件流
            inputStream = Files.newInputStream(Paths.get(filePath));

            dataListMap = readExcelByDataFromInputStream(inputStream, sheetName, clazz);

        } catch (Exception e) {
            throw new EasyExcelException("readExcelByModel from filePath failed." + e, e);
        } finally {
            // 关闭文件流
            try {
                if (null != inputStream) {
                    inputStream.close();
                }
            } catch (IOException e) {
                LOGGER.error("inputStream.close failed!", e);
            }
        }
        return dataListMap;
    }

    public static Map<String, List<ExcelData>> readExcelByDataFromInputStream(InputStream inputStream, String sheetName, Class<ExcelData> clazz) {
        // 解析每行结果在listener中处理
        // 创建返回信息
        Map<String, List<ExcelData>> dataListMap = Maps.newLinkedHashMap();
        // 解析监听器
        //ModelExcelListener excelListener = new ModelExcelListener();
        ExcelDataListener<ExcelData> excelListener = new ExcelDataListener<>();
        try {
            // 创建文件流
            ExcelReader excelReader = EasyExcelFactory.getReader(inputStream, excelListener);

            // 得到所有工作表
            List<Sheet> sheets = excelReader.getSheets();
            // 取所有工作表数据
            for (Sheet sheet : sheets) {
                // 工作表名称
                String currentSheetName = sheet.getSheetName();
                if (Strings.isNullOrEmpty(sheetName) || Splitter.on(',').trimResults().omitEmptyStrings().splitToList(
                        sheetName).contains(currentSheetName)) {
                    // 设置模板
                    sheet.setClazz(clazz);
                    // 读取Excel数据
                    excelReader.read(sheet);
                    // 返回明细数据
                    List<ExcelData> sheetDataInfo = Lists.newArrayList(excelListener.getDataList());
                    // 将工作表数据放入工作薄
                    dataListMap.put(currentSheetName, sheetDataInfo);
                    // 清除缓存数据
                    excelListener.clear();

                }
            }
        } catch (Exception e) {
            throw new EasyExcelException("readExcelByModel from inputStream failed." + e, e);
        }
        return dataListMap;
    }


    private static class ExcelDataListener<T extends BaseRowModel> extends AnalysisEventListener<T> {
        /**
         * 自定义用于暂时存储data 可以通过实例获取该值
         */
        private List<T> dataList = Lists.newArrayList();

        @Override
        public void invoke(T rowInfo, AnalysisContext context) {
            dataList.add(rowInfo);
        }

        /**
         * 解析结束销毁不用的资源
         *
         * @param context AnalysisContext
         */
        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            //解析结束销毁不用的资源
        }

        /**
         * 获取
         *
         * @return 返回sheet数据
         */
        private List<T> getDataList() {
            return dataList;
        }

        /**
         * 设置sheet数据
         *
         * @param dataList 数据
         */
        private void setDataList(List<T> dataList) {
            this.dataList = dataList;
        }

        /**
         * 清空数据
         */
        private void clear() {
            dataList.clear();
        }
    }

    /**
     * EasyExcelException
     */
    public static class EasyExcelException extends RuntimeException {

        private static final long serialVersionUID = -5456062088984840434L;

        public EasyExcelException() {
            super();
        }

        public EasyExcelException(String message) {
            super(message);
        }

        public EasyExcelException(String message, Throwable cause) {
            super(message, cause);
        }

        public EasyExcelException(Throwable cause) {
            super(cause);
        }
    }

}
