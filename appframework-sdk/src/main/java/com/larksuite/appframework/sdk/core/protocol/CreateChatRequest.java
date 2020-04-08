package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class CreateChatRequest {

    private String name;
    private String description;
    private List<String> userIds;
    private List<String> openIds;

    private Boolean shareAllowed;
    private Boolean onlyOwnerAtAll;
    private Boolean onlyOwnerEdit;
    private Boolean onlyOwnerAdd;

    private I18nText i18nNames;
}
