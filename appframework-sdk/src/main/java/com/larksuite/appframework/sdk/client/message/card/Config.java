/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

import com.larksuite.appframework.sdk.utils.MixUtils;

public class Config implements CardComponent {

    private boolean wideScreenMode;

    public Config(boolean wideScreenMode) {
        this.wideScreenMode = wideScreenMode;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap("wide_screen_mode", wideScreenMode);
    }
}
