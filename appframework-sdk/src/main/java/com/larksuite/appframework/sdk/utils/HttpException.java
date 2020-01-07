/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

public class HttpException extends Exception {

    private int httpCode = 0;

    private String response = "";

    public HttpException(int httpCode, String response) {
        super(response);
        this.httpCode = httpCode;
        this.response = String.valueOf(response);
    }

    public HttpException(Exception e) {
        super(e);
    }

    public int getHttpCode() {
        return httpCode;
    }

    public String getResponse() {
        return this.response;
    }

    @Override
    public String getMessage() {
        if (httpCode == 0) {
            return super.getMessage();
        } else {
            return "response http code " + httpCode + " , " + super.getMessage();
        }
    }
}
