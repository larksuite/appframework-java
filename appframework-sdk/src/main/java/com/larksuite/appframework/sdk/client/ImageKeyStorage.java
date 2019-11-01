/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import lombok.Getter;
import lombok.Setter;

public interface ImageKeyStorage {

    @Getter
    @Setter
    class ImageInfo {
        private String imageKey;

        private String md5;

        private Long createTime;
    }

    void persistImageInfo(String appId, String appName, String imageName, ImageInfo imageInfo);

    ImageInfo loadImageInfo(String appId, String appName, String imageName);
}
