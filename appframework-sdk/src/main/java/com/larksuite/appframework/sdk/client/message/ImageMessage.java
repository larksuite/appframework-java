/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message;

public class ImageMessage implements Message {

    private String imageKey;

    private String rootId;

    public ImageMessage(){
    }

    public ImageMessage(String imageKey) {
        this.imageKey = imageKey;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    @Override
    public String getRootId() {
        return rootId;
    }

    @Override
    public String getMsgType() {
        return "image";
    }

    @Override
    public String getContentKey() {
        return "image_key";
    }

    @Override
    public Object getContent() {
        return imageKey;
    }

    public String getImageKey() {
        return imageKey;
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
