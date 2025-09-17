package ${packageName}.model.dto.${dataKey};

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 创建${dataName}请求
 *

 */
@Data
public class ${upperDataKey}AddRequest implements Serializable {

    /**
     * 标题
     */
    @Schema(description = "标题")
    @NotNull
    private String title;

    /**
     * 内容
     */
    @Schema(description = "标题")
    @NotNull
    private String content;

    /**
     * 标签列表
     */
    @Schema(description = "标题")
    @NotNull
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}