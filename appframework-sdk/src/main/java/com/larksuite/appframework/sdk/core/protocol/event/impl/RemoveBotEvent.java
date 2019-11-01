/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event.impl;

import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Event(type = "remove_bot")
public class RemoveBotEvent extends BaseEvent {

    private String chatName;

    private String chatOwnerName;

    private String chatOwnerOpenId;

    private String chatOwnerEmployeeId;

    private String openChatId;

    private String operatorEmployeeId;

    private String operatorName;

    private String operatorOpenId;

    private Boolean ownerIsBot;

    private I18nText chatI18nNames;



}
