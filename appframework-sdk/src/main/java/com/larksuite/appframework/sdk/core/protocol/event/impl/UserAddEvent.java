/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event.impl;

import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import com.larksuite.appframework.sdk.core.protocol.event.BaseUserChangeEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Event(type = "user_add")
public class UserAddEvent extends BaseUserChangeEvent {



}
