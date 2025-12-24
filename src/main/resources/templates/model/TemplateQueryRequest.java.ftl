package ${packageName}.model.dto.${dataKey};

import lombok.Data;
import common.com.vickey.oj.PageRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
<#-- 动态导入实体字段用到的类型（排除 java.lang） -->
<#list importSet as imp>
    import ${imp};
</#list>

@Data
public class ${upperDataKey}QueryRequest extends PageRequest implements Serializable {

<#-- 循环生成字段 -->
<#list fields as f>
    @Schema(description = "${f.description}")
    @NotNull
    private ${f.type} ${f.name};

</#list>
@Schema(description = "搜索关键字")
    private String searchText;

private static final long serialVersionUID = 1L;
}
