/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.objects;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.utils.MixUtils;

public class Field implements CardComponent {

    private boolean isShort;
    private Text text;

    public Field(Text text, boolean isShort) {
        this.isShort = isShort;
        this.text = text;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "is_short", isShort,
                "text", text.toObjectForJson());
    }
}

