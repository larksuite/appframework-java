/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.exception;

public class LarkClientException extends Exception{

    public static final int APP_ACCESS_TOKEN_INVALID = 10013;

    public static final int UNKNOWN_EXCEPTION_ERR_CODE = 9000;
    public static final int REQUEST_FAIL_ERR_CODE = 9001;
    public static final int RESPONSE_DATA_PARSE_ERR_CODE = 9002;

    public static final int TENANT_ACCESS_TOKEN_INVALID = 99991663;

    private final int code;

    public LarkClientException(String msg) {
        this(UNKNOWN_EXCEPTION_ERR_CODE, msg);
    }

    public LarkClientException(int code, String msg) {
        super("code :" + code + ", " + msg);
        this.code = code;
    }

    public LarkClientException(int code, String msg, Throwable e) {
        super("code :" + code + ", " + msg, e);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
