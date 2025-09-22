package com.senaro.springbootinit.generate;

import cn.hutool.core.io.FileUtil;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static cn.hutool.core.text.CharSequenceUtil.upperFirst;

/**
 * 代码生成器
 *

 */
public class CodeGenerator {

    /**
     * 用法：修改生成参数和生成路径，注释掉不需要的生成逻辑，然后运行即可
     *
     * @param args
     * @throws TemplateException
     * @throws IOException
     */
    public static void main(String[] args) throws TemplateException, IOException {
        // ===== ① 基础参数 =====
        String packageName = "com.senaro.springbootinit";
        String dataName = "问题";
        String dataKey = "question";
        String upperDataKey = "Question";

        // ===== ② 指定实体类全限定名 =====
        String entityClassName = packageName + ".model.entity." + upperDataKey;
        Class<?> entityClass = null;
        try {
            entityClass = Class.forName(entityClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        // ===== ③ 解析实体字段 =====
        List<Map<String, String>> fieldList = new ArrayList<>();
        Set<String> importSet = new HashSet<>();

        for (Field field : entityClass.getDeclaredFields()) {
            // 跳过 static / transient 字段
            if (Modifier.isStatic(field.getModifiers()) || Modifier.isTransient(field.getModifiers())) {
                continue;
            }
            Map<String, String> f = new HashMap<>();
            f.put("type", field.getType().getSimpleName());
            f.put("name", field.getName());
            f.put("camelName", upperFirst(field.getName()));
            fieldList.add(f);

            // 记录需额外导入的包（排除 java.lang 和基本类型）
            Package p = field.getType().getPackage();
            if (p != null && !"java.lang".equals(p.getName())) {
                importSet.add(field.getType().getName());
            }
        }

        // ===== ④ 注入模板参数 =====
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("packageName", packageName);
        dataModel.put("dataName", dataName);
        dataModel.put("dataKey", dataKey);
        dataModel.put("upperDataKey", upperDataKey);
        dataModel.put("fields", fieldList);
        dataModel.put("importSet", importSet);

        // ===== ⑤ 调用模板生成 AddRequest（其余 Service / VO 等逻辑照旧）=====
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + "/src/main/resources/templates/model/TemplateAddRequest.java.ftl";
        String outputPath = String.format("%s/generator/model/dto/%s/%sAddRequest.java", projectPath,dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        inputPath = projectPath + "/src/main/resources/templates/model/TemplateEditRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sEditRequest.java", projectPath,dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        inputPath = projectPath + "/src/main/resources/templates/model/TemplateUpdateRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sUpdateRequest.java", projectPath,dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        inputPath = projectPath + "/src/main/resources/templates/model/TemplateQueryRequest.java.ftl";
        outputPath = String.format("%s/generator/model/dto/%s/%sQueryRequest.java", projectPath,dataKey, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 DTO 成功：" + outputPath);
        // 生成 VO
        inputPath = projectPath + File.separator + "src/main/resources/templates/model/TemplateVO.java.ftl";
        outputPath = String.format("%s/generator/model/vo/%sVO.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 VO 成功，文件路径：" + outputPath);
        // 生成 Service
        inputPath = projectPath + File.separator + "src/main/resources/templates/TemplateService.java.ftl";
        outputPath = String.format("%s/generator/service/%sService.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 成功，文件路径：" + outputPath);
        // 生成 ServiceImpl
        inputPath = projectPath + File.separator + "src/main/resources/templates/TemplateServiceImpl.java.ftl";
        outputPath = String.format("%s/generator/service/%sServiceImpl.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Service 成功，文件路径：" + outputPath);
        // 生成 Controller
        inputPath = projectPath + File.separator + "src/main/resources/templates/TemplateController.java.ftl";
        outputPath = String.format("%s/generator/controller/%sController.java", projectPath, upperDataKey);
        doGenerate(inputPath, outputPath, dataModel);
        System.out.println("生成 Controller 成功，文件路径：" + outputPath);
    }

    /**
     * 生成文件
     *
     * @param inputPath  模板文件输入路径
     * @param outputPath 输出路径
     * @param model      数据模型
     * @throws IOException
     * @throws TemplateException
     */
    public static void doGenerate(String inputPath, String outputPath, Object model) throws IOException, TemplateException {
        // new 出 Configuration 对象，参数为 FreeMarker 版本号
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_31);

        // 指定模板文件所在的路径
        File templateDir = new File(inputPath).getParentFile();
        configuration.setDirectoryForTemplateLoading(templateDir);

        // 设置模板文件使用的字符集
        configuration.setDefaultEncoding("utf-8");

        // 创建模板对象，加载指定模板
        String templateName = new File(inputPath).getName();
        Template template = configuration.getTemplate(templateName);

        // 文件不存在则创建文件和父目录
        if (!FileUtil.exist(outputPath)) {
            FileUtil.touch(outputPath);
        }

        // 生成
        Writer out = new FileWriter(outputPath);
        template.process(model, out);

        // 生成文件后别忘了关闭哦
        out.close();
    }
}
