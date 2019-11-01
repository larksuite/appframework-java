/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

import com.larksuite.appframework.sdk.core.protocol.card.CardEvent;

import java.util.Map;

public class CardActionUtils {

    public static final String ACTION_METHOD_NAME = "__METHOD__";

    public static void setActionMethodName(Map<String, String> values, String actionName) {
        if (values != null) {
            values.put(ACTION_METHOD_NAME, actionName);
        }
    }

    public static String getActionMethodName(CardEvent cardEvent) {
        CardEvent.Action action = cardEvent.getAction();
        if (action != null) {

            Map<String, String> value = action.getValue();
            if (value != null) {
                return value.get(ACTION_METHOD_NAME);
            }
        }

        return null;
    }
}
