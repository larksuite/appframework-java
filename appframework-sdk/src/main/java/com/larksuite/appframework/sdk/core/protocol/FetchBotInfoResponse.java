package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.core.protocol.common.BotInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FetchBotInfoResponse extends BaseResponse{
    private BotInfo bot;
}
