/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.InputStream;

@Setter
@Getter
@ToString
public class UploadImageRequest {

    private String originName;

    private InputStream imageFile;
}
