package com.vickey.bi.model.dto.chart;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChartEditRequest implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "分析目标")
    private String goal;

    @Schema(description = "图表数据")
    private String chartData;

    @Schema(description = "图表类型")
    private String chartType;

    @Schema(description = "图表名")
    private String name;


    private static final long serialVersionUID = 1L;
}
