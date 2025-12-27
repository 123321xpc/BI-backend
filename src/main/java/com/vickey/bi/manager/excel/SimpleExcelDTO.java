package com.vickey.bi.manager.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel 数据实体（与 Excel 列名对应）
 */
@Data // Lombok 自动生成get/set/toString
public class SimpleExcelDTO {
    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("价格")
    private Integer price;

}