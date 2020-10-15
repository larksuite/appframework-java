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
public class Url implements CardComponent {

    private String url;

    private String androidUrl;

    private String iosUrl;

    private String pcUrl;

    public Url(String url) {
        this.url = url;
    }

    public Url(String androidUrl, String iosUrl, String pcUrl) {
        this.androidUrl = androidUrl;
        this.iosUrl = iosUrl;
        this.pcUrl = pcUrl;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "url", url,
                "android_url", androidUrl,
                "ios_url", iosUrl,
                "pc_url", pcUrl
        );
    }
}
