package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateChatInfoResponse extends BaseResponse {

    private Data data;

    @Setter
    @Getter
    public static class Data {
        private String chatId;
    }
}

