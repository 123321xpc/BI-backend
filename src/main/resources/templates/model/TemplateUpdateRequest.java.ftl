package ${packageName}.model.dto.${dataKey};

import lombok.Data;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 更新${dataName}请求
 *

 */
@Data
public class ${upperDataKey}UpdateRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull
    private Long id;

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

    private static final long serialVersionUID = 1L;
}