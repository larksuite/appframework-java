package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class AddUserToChatRequest {

    private String chatId;

    private List<String> userIds;

    private List<String> openIds;

}
