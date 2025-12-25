package com.vickey.bi.model.dto.chart;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChartAddRequest implements Serializable {

    @Schema(description = "分析目标")
    @NotNull
    private String goal;

    @Schema(description = "图表数据")
    @NotNull
    private String chartData;

    @Schema(description = "图表类型")
    @NotNull
    private String chartType;

    @Schema(description = "图表名")
    @NotNull
    private String name;


    private static final long serialVersionUID = 1L;
}
