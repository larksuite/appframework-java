package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AddUserToChatResponse extends BaseResponse {

    private Data data;

    @Setter
    @Getter
    public static class Data {
        private List<String> invalidOpenIds;
        private List<String> invalidUserIds;
    }
}
