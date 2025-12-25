package com.vickey.bi.model.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 图表信息表
 * @TableName chart
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chart implements Serializable {


    @Schema(description = "id")
    @NotNull
    private Long id;


    @Schema(description = "分析目标")
    @NotNull
    private String goal;


    @Schema(description = "图表数据")
    @NotNull
    private String chartData;


    @Schema(description = "图表类型")
    @NotNull
    private String chartType;


    @Schema(description = "生成的图表数据")
    @NotNull
    private String genChart;


    @Schema(description = "生成的分析结论")
    @NotNull
    private String genResult;


    @Schema(description = "创建用户 id")
    @NotNull
    private Long userId;


    @Schema(description = "创建时间")
    @NotNull
    private Date createTime;


    @Schema(description = "更新时间")
    @NotNull
    private Date updateTime;


    @Schema(description = "是否删除")
    @NotNull
    private Integer isDelete;


}
