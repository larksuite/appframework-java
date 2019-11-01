/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.card;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class CardEvent {

    private String openId;

    private String userId;

    private String openMessageId;

    private String tenantKey;

    private String token;

    private Action action;

    @Getter
    @Setter
    public static class Action {
        String tag;

        Map<String, String> value;

        private String option;
    }
}
