/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class BaseEvent {

    private String appId;

    private String type;

    private String tenantKey;

}
