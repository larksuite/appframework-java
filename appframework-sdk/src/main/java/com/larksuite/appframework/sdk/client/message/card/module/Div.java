/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.client.message.card.element.Element;
import com.larksuite.appframework.sdk.client.message.card.objects.Field;
import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Div extends Module {

    private Text text;

    private List<Field> fields;

    private Element extra;

    public Div(Text text, List<Field> fields, Element extra) {
        super("div");
        this.text = text;
        this.fields = fields;
        this.extra = extra;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = MixUtils.newHashMap("tag", getTag());

        if (text != null) {
            r.put( "text", text.toObjectForJson());
        }

        if (fields != null) {
            r.put("fields", fields.stream().map(Field::toObjectForJson).collect(Collectors.toList()));
        }

        if (extra != null) {
            r.put("extra", extra.toObjectForJson());
        }
        return r;
    }
}
