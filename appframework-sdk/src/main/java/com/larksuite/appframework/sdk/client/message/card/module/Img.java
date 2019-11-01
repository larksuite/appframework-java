/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;

import java.util.Map;

public class Img extends Module {

    private String imgKey;

    private Text alt;

    private Text title;

    public Img(String imgKey, Text alt, Text title) {
        super("img");
        this.imgKey = imgKey;
        this.alt = alt;
        this.title = title;
    }

    @Override
    public Object toObjectForJson() {

        Map<String, Object> r = MixUtils.newHashMap("tag", getTag(),
                "alt", alt.toObjectForJson(),
                "img_key", imgKey
        );

        if (title != null) {
            r.put("title", title.toObjectForJson());
        }

        return r;
    }
}
