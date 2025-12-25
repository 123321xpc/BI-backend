package com.vickey.bi.model.dto.chart;

import lombok.Data;
import common.com.vickey.bi.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
    import java.util.Date;

@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

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

@Schema(description = "搜索关键字")
    private String searchText;

private static final long serialVersionUID = 1L;
}
