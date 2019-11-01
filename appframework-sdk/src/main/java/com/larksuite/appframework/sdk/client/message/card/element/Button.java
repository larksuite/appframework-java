/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Confirm;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import com.larksuite.appframework.sdk.client.message.card.objects.Url;

import java.util.Map;

public class Button extends ActionElement {

    public enum StyleType {
        DEFAULT,
        PRIMARY,
        DANGER
    }

    private Text text;

    private String url;

    private Url multiUrl;

    private StyleType type = StyleType.DEFAULT;

    private Confirm confirm;

    public Button(String methodName, Text text) {
        super("button", methodName);
        this.text = text;
    }

    public Button setType(StyleType s) {
        this.type = s;
        return this;
    }

    public Button setConfirm(Confirm c) {
        this.confirm = c;
        return this;
    }

    public Button setValue(Map<String, String> v) {
        super.addActionValues(v);
        return this;
    }

    public Button setUrl(String url) {
        this.url = url;
        this.multiUrl = null;
        return this;
    }

    public Button setMultiUrl(Url url) {
        this.multiUrl = url;
        this.url = null;
        return this;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = MixUtils.newHashMap("tag", getTag());

        if (text != null) {
            r.put("text", text.toObjectForJson());
        }

        if (url != null) {
            r.put("url", url);
        }

        if (multiUrl != null) {
            r.put("multi_url", multiUrl.toObjectForJson());
        }

        if (type != null) {
            r.put("type", type.name().toLowerCase());
        }

        r.put("value", getValue());

        if (confirm != null) {
            r.put("confirm", confirm.toObjectForJson());
        }

        return r;
    }
}
