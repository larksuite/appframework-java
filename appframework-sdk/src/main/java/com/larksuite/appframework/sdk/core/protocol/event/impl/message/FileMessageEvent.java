package com.larksuite.appframework.sdk.core.protocol.event.impl.message;

import com.larksuite.appframework.sdk.annotation.MessageEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@MessageEvent(type = "file")
public class FileMessageEvent extends BaseMessageEvent {

    private String fileKey;
}
