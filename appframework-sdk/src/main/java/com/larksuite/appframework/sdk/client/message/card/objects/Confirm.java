/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.objects;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.utils.MixUtils;

public class Confirm implements CardComponent {

    private Text title;

    private Text text;

    public Confirm(Text title, Text text) {
        this.title = title;
        this.text = text;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "title", title.toObjectForJson(),
                "text", text.toObjectForJson()
        );
    }
}
