/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import com.larksuite.appframework.sdk.utils.MixUtils;

public class Header implements CardComponent {

    private Text title;

    public Header(Text title) {
        this.title = title;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap("title", title.toObjectForJson());
    }
}
