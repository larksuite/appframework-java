/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.objects;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.utils.MixUtils;

import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Option implements CardComponent {

    private String value;

    private Text text;

    private String url;

    private Url multiUrl;

    public Option(String value, Text text, String url) {
        this(value, text);
        this.url = url;
    }

    public Option(String value, Text text, Url multiUrl) {
        this(value, text);
        this.multiUrl = multiUrl;
    }

    public Option(String value, Text text) {
        this.value = value;
        this.text = text;
    }

    @Override
    public Object toObjectForJson() {

        Map<String, Object> m = MixUtils.newHashMap(
                "text", text.toObjectForJson(),
                "url", url,
                "value", value
        );

        if (multiUrl != null) {
            m.put("multi_url", multiUrl.toObjectForJson());
        }

        if (text != null) {
            m.put("text", text.toObjectForJson());
        }

        return m;
    }
}
