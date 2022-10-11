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

    @ApiModelProperty(value="工时填报日期", dataType = "String", required = true, example = "202201")
    @ExcelProperty("工时填报日期")
    private String fillDate;

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

    @ApiModelProperty(value="工时分类", dataType = "String", required = true, example = "培训及学习类")
    @ExcelProperty("工时分类")
    private String hourType;

    @ApiModelProperty(value="项目编号", dataType = "String", required = true, example = "CP000")
    @ExcelProperty("项目编号")
    private String projectNum;

    @ApiModelProperty(value="项目名称", dataType = "String", required = true, example = "one平台")
    @ExcelProperty("项目名称")
    private String projectName;

    @ApiModelProperty(value="工时", dataType = "String", required = true, example = "3.00")
    @ExcelProperty("工时")
    private Double hour;


}
