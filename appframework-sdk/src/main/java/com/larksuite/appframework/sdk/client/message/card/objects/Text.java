/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.objects;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;

import java.util.HashMap;
import java.util.Map;

public class Text implements CardComponent {

    public enum Mode {
        PLAIN_TEXT,
        LARK_MD
    }

    private String content;

    private I18n i18n;

    private Mode m;

    private Integer lines;

    public Text(Mode m, String content) {
        this(m, content, 0);
    }

    public Text(Mode m, I18n i18n) {
        this(m, i18n, 0);
    }

    public Text(Mode m, String content, int lines) {
        this.m = m;
        this.content = content;
        this.lines = lines;
    }

    public Text(Mode m, I18n i18n, int lines) {
        this.m = m;
        this.i18n = i18n;
        this.lines = lines;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = new HashMap<>(2);
        r.put("tag", m.name().toLowerCase());
        if (content != null) {
            r.put("content", content);
        }
        if (i18n != null) {
            r.put("i18n", i18n.toObjectForJson());
        }
        if (lines != null) {
            r.put("lines", lines);
        }
        return r;
    }
}
