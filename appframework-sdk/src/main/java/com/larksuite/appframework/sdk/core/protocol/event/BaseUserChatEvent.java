/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event;

import com.larksuite.appframework.sdk.core.protocol.common.NamedUser;
import com.larksuite.appframework.sdk.core.protocol.common.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public abstract class BaseUserChatEvent extends BaseEvent {

    private String chatId;

    private List<NamedUser> users;

    private User operator;
}
