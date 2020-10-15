/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Image extends Element {

    private String imgKey;

    private Text alt;

    public Image(String imgKey, Text alt) {
        super("img");
        this.imgKey = imgKey;
        this.alt = alt;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "tag", getTag(),
                "img_key", imgKey,
                "alt", alt.toObjectForJson()
        );
    }
}
