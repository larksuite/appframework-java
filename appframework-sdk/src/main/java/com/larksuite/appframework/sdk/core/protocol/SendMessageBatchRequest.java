/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class SendMessageBatchRequest {

    private List<String> departmentIds;

    private List<String> openIds;

    private List<String> userIds;

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
