package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class DeleteUserFromChatRequest {

    String chatId;

    List<String> userIds;

    List<String> openIds;
}
