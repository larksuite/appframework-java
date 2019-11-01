/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event.impl;

import com.larksuite.appframework.sdk.core.protocol.common.User;
import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.core.protocol.common.NamedUser;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Event(type = "p2p_chat_create")
public class P2pChatCreateEvent extends BaseEvent {

    private String chatId;

    private User operator;

    private NamedUser user;


}
