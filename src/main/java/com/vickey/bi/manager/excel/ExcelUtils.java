package com.vickey.bi.manager.excel;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ExcelUtils {

    public static List<SimpleExcelDTO> readExcel(MultipartFile file) {


        try (InputStream inputStream = file.getInputStream()) {
            // 调用读取方法
            return SimpleExcelReader.readExcel(inputStream, SimpleExcelDTO.class);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("读取失败：" + e.getMessage());
        }

        return null;
    }

    public static String excelToCsv(MultipartFile file) {

        return Objects.requireNonNull(readExcel(file)).stream()
                .filter(Objects::nonNull)
                .map(dto -> String.join(",",
                        StringUtils.isNotBlank(dto.getName()) ? dto.getName() : "",
                        StringUtils.isNotBlank(dto.getAge().toString()) ? dto.getAge().toString() : "",
                        StringUtils.isNotBlank(dto.getPhone()) ? dto.getPhone() : ""
                ))
                .collect(Collectors.joining("\n"));
    }
}
