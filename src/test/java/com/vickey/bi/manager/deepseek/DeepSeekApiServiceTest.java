package com.vickey.bi.manager.deepseek;

import com.vickey.bi.manager.excel.ExcelUtils;
import com.vickey.bi.manager.excel.SimpleExcelDTO;
import com.vickey.bi.manager.excel.SimpleExcelReader;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

import java.io.FileInputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeepSeekApiServiceTest {

    @Resource
    private DeepSeekApiService deepSeekApiService;

    private static final String userMsg = "【【【【\n" +
            "\n" +
            "{{分析数据的增长趋势，并预测下5个月的数据内容}}\n" +
            "\n" +
            "{{\n" +
            "\n" +
            "1月,3.5\n" +
            "\n" +
            "2月,4.5\n" +
            "\n" +
            "3月,5.5\n" +
            "\n" +
            "4月,6.0\n" +
            "\n" +
            "5月,6.5\n" +
            "\n" +
            "6月,6.8\n" +
            "\n" +
            "}}\n" +
            "\n" +
            "】】】】";

    @Test
    void chatWithDeepSeek() {
        System.out.println(deepSeekApiService.chatWithDeepSeek(userMsg));
    }

    @Test
    void testReadExcel() {
//        ExcelUtils.excelToCsv("", "");
    }
}