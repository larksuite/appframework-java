/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

public interface HttpClient {

    String doGet(String url, int connectTimeout, int readTimeout, Map<String, String> headers) throws HttpException;

    String doPostJson(String url, int connectTimeout, int readTimeout, Map<String, String> headers, String data) throws HttpException;

    String doPostFile(String url, int connectTimeout, int readTimeout, Map<String, String> headers, List<FileField> files) throws HttpException;

    @Setter
    @Getter
    @AllArgsConstructor
    class FileField {

        String fieldName;

        InputStream is;

        String fileName;
    }
}
