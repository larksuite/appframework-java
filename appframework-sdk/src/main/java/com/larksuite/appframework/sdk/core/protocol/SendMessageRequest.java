/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SendMessageRequest {

    private String openId;

    private String chatId;

    private String userId;

    private String email;

    private String rootId;

    private String msgType;

    private Object content;

    /**
     * for card message
     */
    private Boolean updateMulti;

    /**
     * for card message
     */
    private Object card;
}
