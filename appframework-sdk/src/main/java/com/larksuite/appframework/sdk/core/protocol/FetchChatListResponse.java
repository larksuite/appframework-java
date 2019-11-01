/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.core.protocol.common.Group;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class FetchChatListResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {

        private List<Group> groups;

        private Boolean hasMore;

        private String pageToken;
    }
}
