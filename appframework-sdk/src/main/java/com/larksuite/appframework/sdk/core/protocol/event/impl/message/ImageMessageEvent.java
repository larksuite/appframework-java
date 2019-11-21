package com.larksuite.appframework.sdk.core.protocol.event.impl.message;

import com.larksuite.appframework.sdk.annotation.MessageEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@MessageEvent(type = "image")
public class ImageMessageEvent extends BaseMessageEvent {

    private String imageHeight;

    private String imageWidth;

    private String imageKey;

}
