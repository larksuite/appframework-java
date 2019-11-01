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
public class SendMessageBatchResponse extends BaseResponse {

    private Data data;

    @Setter
    @Getter
    @ToString
    public static class Data {
        private List<String> invalidDepartmentIds;

        private List<String> invalidOpenIds;

        private List<String> invalidUserIds;

        private String messageId;
    }
}
