package com.vickey.bi.model.dto.chart;

import com.vickey.bi.common.PageRequest;
import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

@Data
public class ChartQueryRequest extends PageRequest implements Serializable {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "分析目标")
    private String goal;

    @Schema(description = "图表类型")
    private String chartType;


    @Schema(description = "搜索关键字")
    private String searchText;


    @Schema(description = "创建用户 id")
    private Long userId;


    private static final long serialVersionUID = 1L;
}
