/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.auth;

public class AppAccessToken extends Token {

    private String appId;

    public AppAccessToken(String appId, String token, long expireTime) {
        super(token, expireTime);
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }
}
