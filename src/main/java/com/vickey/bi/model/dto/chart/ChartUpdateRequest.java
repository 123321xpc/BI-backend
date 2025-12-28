package com.vickey.bi.model.dto.chart;

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChartUpdateRequest implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "图表名")
    private String name;

    @Schema(description = "分析目标")
    private String goal;

    @Schema(description="执行信息")
    private String execMsg;

    @Schema(description="图表状态 - processing, success, failure")
    private String status;

    @Schema(description = "图表数据")
    private String chartData;

    @Schema(description = "图表类型")
    private String chartType;

    @Schema(description = "生成的图表数据")
    private String genChart;

    @Schema(description = "生成的分析结论")
    private String genResult;

    private static final long serialVersionUID = 1L;
}
