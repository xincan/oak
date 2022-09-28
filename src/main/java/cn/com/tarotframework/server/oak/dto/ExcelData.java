package cn.com.tarotframework.server.oak.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@ApiModel(description = "导入数据接收实体")
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ExcelData extends BaseRowModel {

    @ApiModelProperty(value="月份", dataType = "String", required = true, example = "202201")
    @ExcelProperty("月份")
    private String month;

    @ApiModelProperty(value="工时填报人", dataType = "String", required = true, example = "张三")
    @ExcelProperty("工时填报人")
    private String name;

    @ApiModelProperty(value="区域", dataType = "String", required = true, example = "北京")
    @ExcelProperty("区域")
    private String area;

    @ApiModelProperty(value="一级部门", dataType = "String", required = true, example = "研发中心")
    @ExcelProperty("一级部门")
    private String oneDepart;

    @ApiModelProperty(value="部门", dataType = "String", required = true, example = "架构部")
    @ExcelProperty("部门")
    private String twoDepart;

    @ApiModelProperty(value="产品线", dataType = "String", required = true, example = "部门内部培训")
    @ExcelProperty("产品线")
    private String productType;

    @ApiModelProperty(value="工时分类", dataType = "String", required = true, example = "培训及学习类")
    @ExcelProperty("工时分类")
    private String hourType;

    @ApiModelProperty(value="编号", dataType = "String", required = true, example = "部门内部培训")
    @ExcelProperty("编号")
    private String num;

    @ApiModelProperty(value="工时", dataType = "String", required = true, example = "3.00")
    @ExcelProperty("工时")
    private Double hour;

    @ApiModelProperty(value="人天", dataType = "String", required = true, example = "0.30")
    @ExcelProperty("人天")
    private Double personDay;

    @JsonIgnore
    private String projectNum;

    @JsonIgnore
    private String projectName;

    @JsonIgnore
    private LocalDate workHour;

}
