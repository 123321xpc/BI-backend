package com.vickey.bi.manager.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * Excel 数据实体（与 Excel 列名对应）
 */
@Data // Lombok 自动生成get/set/toString
public class SimpleExcelDTO {
    // 列名：姓名（与Excel表头一致）
    @ExcelProperty("姓名")
    private String name;

    // 列名：年龄
    @ExcelProperty("年龄")
    private Integer age;

    // 列名：手机号
    @ExcelProperty("手机号")
    private String phone;
}