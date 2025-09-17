package ${packageName}.model.dto.${dataKey};

import ${packageName}.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 查询${dataName}请求
 *

 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ${upperDataKey}QueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @Schema(description = "id")
    @NotNull
    private Long id;

    /**
     * 搜索词
     */
    @Schema(description = "搜索词")
    @NotNull
    private String searchText;

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