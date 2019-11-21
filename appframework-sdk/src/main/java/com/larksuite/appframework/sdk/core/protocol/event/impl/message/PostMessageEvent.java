package com.larksuite.appframework.sdk.core.protocol.event.impl.message;

import com.larksuite.appframework.sdk.annotation.MessageEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
@MessageEvent(type = "post")
public class PostMessageEvent extends BaseMessageEvent {

    private String text;

    private String textWithoutAtBot;

    private String title;

    private List<String> imageKeys;
}
