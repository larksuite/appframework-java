/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

import com.larksuite.appframework.sdk.client.message.card.objects.Text;
import com.larksuite.appframework.sdk.utils.MixUtils;

import java.util.Map;

public class Header implements CardComponent {

    private Text title;

    private TemplateColor templateColor;

    public Header(Text title) {
        this.title = title;
    }

    public Header(Text text, TemplateColor templateColor){
        this.title = text;
        this.templateColor = templateColor;
    }

    @Override
    public Object toObjectForJson() {

        Map headerMap = MixUtils.newHashMap("title", title.toObjectForJson());
        if (this.templateColor != null){

            headerMap.put("template", templateColor.getColor());
        }

        return headerMap;
    }
}
