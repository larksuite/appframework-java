/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.objects;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.utils.MixUtils;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class I18n implements CardComponent {

    private String zhCn;

    private String enUs;

    private String jaJp;

    public I18n(String zhCn, String enUs, String jaJp) {
        this.zhCn = zhCn;
        this.enUs = enUs;
        this.jaJp = jaJp;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "zh_cn", zhCn,
                "en_us", enUs,
                "ja_jp", jaJp);
    }
}
