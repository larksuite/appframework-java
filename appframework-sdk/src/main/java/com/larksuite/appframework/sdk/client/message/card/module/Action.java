/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.element.Element;

import java.util.List;
import java.util.stream.Collectors;

public class Action extends Module {

    private List<Element> actions;

    public Action(List<Element> actions) {
        super("action");
        this.actions = actions;
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "tag", getTag(),
                "actions", actions.stream().map(CardComponent::toObjectForJson).collect(Collectors.toList()));
    }
}
