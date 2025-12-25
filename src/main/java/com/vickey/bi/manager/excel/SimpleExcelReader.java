package com.vickey.bi.manager.excel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 极简 Excel 读取工具类
 */
public class SimpleExcelReader {

    /**
     * 读取 Excel 文件（通用方法）
     *
     * @param inputStream Excel 文件输入流（支持本地文件/上传文件）
     * @param clazz       映射的实体类Class
     * @param <T>         泛型
     * @return 解析后的数据集
     */
    public static <T> List<T> readExcel(InputStream inputStream, Class<T> clazz) {
        // 1. 定义列表存储读取结果
        List<T> dataList = new ArrayList<>();

        // 2. 创建 EasyExcel 监听器（极简版，仅存数据）
        AnalysisEventListener<T> listener = new AnalysisEventListener<T>() {
            // 每读取一行数据，添加到列表
            @Override
            public void invoke(T data, AnalysisContext context) {
                dataList.add(data);
            }

            // 读取完成后无需额外操作
            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("Excel 读取完成，共读取 " + dataList.size() + " 行数据");
            }
        };

        // 3. 执行读取（默认读取第一个sheet，表头1行）
        EasyExcel.read(inputStream, clazz, listener)
                .sheet() // 读取第一个工作表
                .headRowNumber(1) // 表头行数（默认1行）
                .doRead();

        // 4. 返回读取结果
        return dataList;
    }
}