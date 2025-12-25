package com.vickey.bi.manager.deepseek;

import lombok.Data;

import java.util.List;

@Data
public class DeepSeekChatResponse {
    /**
     * 响应ID
     */
    private String id;
    /**
     * 对象类型
     */
    private String object;
    /**
     * 时间戳
     */
    private long created;
    /**
     * 模型名称
     */
    private String model;
    /**
     * 响应内容列表
     */
    private List<Choice> choices;
    /**
     * 用量统计
     */
    private Usage usage;

    // 响应选项子实体
    @Data
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;

        @Data
        public static class Message {
            private String role;
            private String content;
        }
    }

    // 用量子实体
    @Data
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }
}