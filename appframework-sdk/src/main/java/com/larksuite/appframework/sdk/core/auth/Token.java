/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.auth;

public class Token {

    private final String token;

    private final long expireTime;

    public Token(String token, long expireTime) {
        this.token = token;
        this.expireTime = expireTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public String getToken() {
        return token;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() >= expireTime;
    }
}
