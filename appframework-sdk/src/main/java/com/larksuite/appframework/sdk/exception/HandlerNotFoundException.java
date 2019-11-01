/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.exception;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(String msg) {
        super(msg);
    }
}
