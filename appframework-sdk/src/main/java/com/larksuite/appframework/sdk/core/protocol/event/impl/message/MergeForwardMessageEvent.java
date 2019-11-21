package com.larksuite.appframework.sdk.core.protocol.event.impl.message;

import com.larksuite.appframework.sdk.annotation.MessageEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@MessageEvent(type = "merge_forward")
public class MergeForwardMessageEvent extends BaseMessageEvent {

    private String chatId;

    private String user;

    private List<Message> msgList;

    @Setter
    @Getter
    @ToString
    public static class Message {
        private String rootId;
        private String parentId;
        private String openChatId;
        private String msgType;
        private String openId;
        private String openMessageId;
        private Boolean isMention;
        private Long createTime;

        // post message / text message
        private String text;

        // post message
        private String title;

        // image message
        private String imageKey;
        private String imageUrl;
    }
}
