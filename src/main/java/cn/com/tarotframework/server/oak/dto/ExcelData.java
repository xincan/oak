package cn.com.tarotframework.server.oak.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExcelData extends BaseRowModel {

    @ExcelProperty("月份")
    private String month;

    @ExcelProperty("工时填报人")
    private String name;

    @ExcelProperty("区域")
    private String area;

    @ExcelProperty("一级部门")
    private String oneDepart;

    @ExcelProperty("部门")
    private String twoDepart;

    @ExcelProperty("产品线")
    private String productType;

    @ExcelProperty("工时分类")
    private String hourType;

    @ExcelProperty("编号")
    private String num;

    @ExcelProperty("工时")
    private Double hour;

    @ExcelProperty("人天")
    private Double personDay;

    private String projectNum;

    private String projectName;

    private LocalDate workHour;

}
