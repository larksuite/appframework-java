package com.larksuite.appframework.sdk.client;

import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class ChatInfo {
    private String ownerUserId;
    private String ownerOpenId;

    private List<String> openIds;
    private List<String> userIds;

    private String name;
    private I18nText i18nNames;

    private Boolean onlyOwnerAdd;
    private Boolean shareAllowed;
    private Boolean onlyOwnerAtAll;
    private Boolean onlyOwnerEdit;

}

