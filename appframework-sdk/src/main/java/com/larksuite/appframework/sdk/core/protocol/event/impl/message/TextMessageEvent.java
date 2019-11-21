package com.larksuite.appframework.sdk.core.protocol.event.impl.message;

import com.larksuite.appframework.sdk.annotation.MessageEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@MessageEvent(type = "text")
public class TextMessageEvent extends BaseMessageEvent {

    private String text;

    private String textWithoutAtBot;
}
