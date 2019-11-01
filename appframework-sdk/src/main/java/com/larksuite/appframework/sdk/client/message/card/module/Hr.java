/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.utils.MixUtils;

public class Hr extends Module {

    public Hr() {
        super("hr");
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap("tag", getTag());
    }
}
