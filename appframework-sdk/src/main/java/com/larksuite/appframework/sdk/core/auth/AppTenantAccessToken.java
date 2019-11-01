/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.auth;

public class AppTenantAccessToken extends Token {

    private final String appId;

    private final String tenantKey;

    public AppTenantAccessToken(String appId, String tenantKey, String token, long expireTime) {
        super(token, expireTime);
        this.appId = appId;
        this.tenantKey = tenantKey;
    }

    public String getAppId() {
        return appId;
    }

    public String getTenantKey() {
        return tenantKey;
    }
}
