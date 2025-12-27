package com.vickey.bi.manager.deepseek;

import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class DeepSeekApiService {

    // 从配置文件读取 API Key
    @Value("${deepseek.api.key}")
    private String apiKey;

    // DeepSeek 对话 API 地址
    private static final String DEEPSEEK_CHAT_API = "https://api.deepseek.com/v1/chat/completions";

    // 完整的 System 指令（定义 AI 角色/行为准则，可根据业务调整）
    private static final String SYSTEM_MESSAGE = "你是一个数据分析师和前端开发专家，接下来我会按照以下格式给你提供内容：\n" +
            "\n" +
            "【【【【\n" +
            "\n" +
            "{{具体分析目标，一段文字}}\n" +
            "\n" +
            "{{需要分析的数据内容，csv格式，用,进行分割}}\n" +
            "\n" +
            "{{所需要生成图表的类型，例如柱状图、折线图等。图表类型可能为空，若为空，则自动选择最合适的图表类型}}\n" +
            "\n" +
            "】】】】\n" +
            "\n" +
            "请根据上述内容进行分析，并严格按照如下格式生成内容。\n" +
            "\n" +
            "【【【生成的图表名，名称最后需标明是什么类型的图表。20字以内】】】\n" +
            "\n" +
            "【【【具体的分析结果，700字以内】】】\n" +
            "\n" +
            "【【【对应的echarts V5版本的options对象的js代码，使用json格式，图表名设置为之前生成的图表名，在title中添加bottom: '10px';left: 'center' 和tooltip属性。不需要前边的options=以及最后的;】】】\n" +
            "\n" +
            "【【【生成的图表类型，10字以内】】】\n" +
            "\n" +
            "注意：不要生成任何其他多余内容，包括注释！";

    @Resource
    private RestTemplate restTemplate;

    public List<String> extractContent(String content) {
        // 核心修复：
        // 1. 使用Unicode转义符匹配【】，避免字符编码问题
        // 2. 添加Pattern.DOTALL模式，让.匹配包括换行在内的所有字符
        String regex = "\\u3010{3}(.*?)\\u3011{3}";
        Pattern pattern = Pattern.compile(regex, Pattern.DOTALL); // 关键：DOTALL模式必须加
        Matcher matcher = pattern.matcher(content);

        List<String> resultList = new ArrayList<>();
        while (matcher.find()) {
            // 先判断group(1)是否为空，避免空指针
            String matchContent = matcher.group(1) == null ? "" : matcher.group(1).trim();
            // 即使内容包含换行/空格，只要非空就加入结果（可根据需求调整trim()）
            if (!matchContent.isEmpty()) {
                resultList.add(matchContent);
            }
        }
        return resultList;
    }

    /**
     * 调用 DeepSeek 对话 API（正确传入 system 消息）
     *
     * @param userMessage 用户提问内容
     * @return 助手回复内容
     */
    public String chatWithDeepSeek(String userMessage) {
        // 1. 构造请求头（携带 API Key）
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // 等价于 headers.add("Authorization", "Bearer " + apiKey);

        // 2. 构造请求参数（核心：system 消息 + user 消息）
        DeepSeekChatRequest request = new DeepSeekChatRequest();
        // 关键：messages 列表中，system 消息放最前面，user 消息紧随其后
        request.setMessages(Arrays.asList(
                // System 消息：定义 AI 的行为准则（优先级最高）
                DeepSeekChatRequest.Message.of("system", SYSTEM_MESSAGE),
                // User 消息：用户的实际提问
                DeepSeekChatRequest.Message.of("user", userMessage)
        ));
        // 可选：设置模型参数（根据需求调整）
        request.setModel("deepseek-chat"); // 指定模型版本
        request.setTemperature(0.7); // 随机性（0-1）
        request.setMax_tokens(2048); // 最大回复长度

        // 3. 封装请求体
        HttpEntity<String> requestEntity = new HttpEntity<>(
                JSONUtil.toJsonStr(request),
                headers
        );

        // 4. 发送 POST 请求并解析响应
        try {
            ResponseEntity<DeepSeekChatResponse> response = restTemplate.exchange(
                    DEEPSEEK_CHAT_API,
                    HttpMethod.POST,
                    requestEntity,
                    DeepSeekChatResponse.class
            );

            // 5. 提取响应内容
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                // 校验 choices 非空
                if (!response.getBody().getChoices().isEmpty()) {
                    DeepSeekChatResponse.Choice choice = response.getBody().getChoices().get(0);
                    return choice.getMessage().getContent();
                }
                return "DeepSeek 未返回有效回复";
            } else {
                return "请求失败，状态码：" + response.getStatusCode() + "，响应体：" + response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "调用 DeepSeek API 异常：" + e.getMessage();
        }
    }

    // 重载方法：支持自定义 system 指令（灵活扩展）
    public String chatWithDeepSeek(String userMessage, String customSystemMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        DeepSeekChatRequest request = new DeepSeekChatRequest();
        request.setMessages(Arrays.asList(
                DeepSeekChatRequest.Message.of("system", customSystemMessage),
                DeepSeekChatRequest.Message.of("user", userMessage)
        ));

        HttpEntity<String> requestEntity = new HttpEntity<>(JSONUtil.toJsonStr(request), headers);
        try {
            ResponseEntity<DeepSeekChatResponse> response = restTemplate.exchange(
                    DEEPSEEK_CHAT_API, HttpMethod.POST, requestEntity, DeepSeekChatResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().getChoices().isEmpty()) {
                return response.getBody().getChoices().get(0).getMessage().getContent();
            }
            return "请求失败或无有效回复";
        } catch (Exception e) {
            e.printStackTrace();
            return "API 调用异常：" + e.getMessage();
        }
    }
}