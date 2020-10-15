/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message;

public class TextMessage implements Message {

    private String textContent;

    private String rootId;

    public TextMessage(String textContent) {
        this.textContent = textContent;
    }

    public TextMessage() {
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
        return "text";
    }

    @Override
    public String getContentKey() {
        return "text";
    }

    @Override
    public Object getContent() {
        return textContent;
    }
}
