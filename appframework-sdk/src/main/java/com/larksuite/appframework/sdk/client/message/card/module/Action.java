/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.client.message.card.Layout;
import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.element.Element;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Action extends Module {

    private List<Element> actions;

    private Layout layout;

    public Action(List<Element> actions) {
        super("action");
        this.actions = actions;
    }

    public Action(List<Element> actions, Layout layout) {
        this(actions);
        this.layout = layout;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = MixUtils.newHashMap(
                "tag", getTag(),
                "actions", actions.stream().map(CardComponent::toObjectForJson).collect(Collectors.toList()));

        if (layout != null) {
            r.put("layout", layout.asValue());
        }

        return r;
    }
}
