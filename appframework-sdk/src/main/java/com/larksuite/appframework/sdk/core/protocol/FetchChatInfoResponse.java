/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import com.larksuite.appframework.sdk.core.protocol.common.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Setter
@Getter
@ToString
public class FetchChatInfoResponse extends BaseResponse {

    private Data data;

    @ToString
    @Setter
    @Getter
    public static class Data {

        private String avatar;

        private String chatId;

        private String description;

        private I18nText i18nNames;

        private List<User> members;

        private String name;

        private String ownerOpenId;

        private String ownerUserId;
    }
}
