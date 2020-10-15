/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message;

public class ShareGroupMessage implements Message {

    private String shareChatId;

    private String rootId;

    public ShareGroupMessage(String shareChatId) {
        this.shareChatId = shareChatId;
    }

    public void setRootId(String rootId) {
        this.rootId = rootId;
    }

    @Override
    public String getRootId() {
        return rootId;
    }

    public ShareGroupMessage() {
    }

    @Override
    public String getMsgType() {
        return "share_chat";
    }

    @Override
    public String getContentKey() {
        return "share_chat_id";
    }

    @Override
    public Object getContent() {
        return shareChatId;
    }
}
