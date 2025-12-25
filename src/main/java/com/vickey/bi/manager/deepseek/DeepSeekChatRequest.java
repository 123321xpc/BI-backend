package com.vickey.bi.manager.deepseek;

import lombok.Data;

import java.util.List;

@Data
public class DeepSeekChatRequest {
    /**
     * 模型名称，如 deepseek-chat、deepseek-coder
     */
    private String model = "deepseek-chat";
    /**
     * 对话消息列表
     */
    private List<Message> messages;
    /**
     * 温度，0-1 之间，值越高越随机
     */
    private double temperature = 0.7;
    /**
     * 最大生成token数
     */
    private int max_tokens = 2048;

    // 消息子实体
    @Data
    public static class Message {
        /**
         * 角色：user（用户）、assistant（助手）、system（系统）
         */
        private String role;
        /**
         * 消息内容
         */
        private String content;

        // 快捷构造方法
        public static Message of(String role, String content) {
            Message message = new Message();
            message.setRole(role);
            message.setContent(content);
            return message;
        }
    }
}