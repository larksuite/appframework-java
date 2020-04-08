package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UpdateChatInfoRequest {

    private String chatId;
    private String ownerUserId;
    private String ownerOpenId;
    private String name;

    private Boolean onlyOwnerAdd;
    private Boolean shareAllowed;
    private Boolean onlyOwnerAtAll;
    private Boolean onlyOwnerEdit;

    private I18nText i18nNames;
}

