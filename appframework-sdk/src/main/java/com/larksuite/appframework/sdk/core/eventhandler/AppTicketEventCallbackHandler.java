/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.eventhandler;

import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppTicketEvent;

public class AppTicketEventCallbackHandler implements EventCallbackHandler<AppTicketEvent> {
    @Override
    public Object handler(InstanceContext c, AppTicketEvent event) {
        c.getTokenCenter().refreshAppTicket(event.getAppTicket());
        return null;
    }
}
