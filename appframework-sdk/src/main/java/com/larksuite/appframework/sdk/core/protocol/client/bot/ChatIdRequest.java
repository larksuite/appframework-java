package com.larksuite.appframework.sdk.core.protocol.client.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/19
 */
@Data
public class ChatIdRequest {
    @JsonProperty("chat_id")
    private String chatId;
}
