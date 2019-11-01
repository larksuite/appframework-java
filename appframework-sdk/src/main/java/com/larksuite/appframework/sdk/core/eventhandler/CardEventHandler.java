/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.eventhandler;

import com.larksuite.appframework.sdk.core.protocol.card.CardEvent;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.core.InstanceContext;

public interface CardEventHandler {

    Card handle(InstanceContext c, CardEvent e);
}
