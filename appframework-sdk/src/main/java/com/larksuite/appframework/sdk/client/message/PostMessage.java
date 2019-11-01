/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message;

public class PostMessage implements Message {

    private String rootId;

    private Object content;

    public PostMessage(Object content) {
        this.content = content;
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
        return "post";
    }

    @Override
    public String getContentKey() {
        return "post";
    }

    @Override
    public Object getContent() {
        return content;
    }
}
