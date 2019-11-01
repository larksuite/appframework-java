/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class MiniProgramLoginResponse extends BaseResponse {

    private Data data;

    @Getter
    @Setter
    @ToString
    public static class Data {

        @Deprecated
        private String uid;

        private String openId;

        private String unionId;

        private String sessionKey;

        private String tenantKey;

        private String employeeId;

        private String tokenType;

        private String accessToken;

        private String expiresIn;

        private String refreshToken;
    }
}
