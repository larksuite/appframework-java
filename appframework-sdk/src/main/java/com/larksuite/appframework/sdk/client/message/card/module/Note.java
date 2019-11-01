/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.module;

import com.larksuite.appframework.sdk.client.message.card.CardComponent;
import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.element.Image;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;

import java.util.List;
import java.util.stream.Collectors;

public class Note extends Module {

    private List<CardComponent> elements;

    public Note(List<CardComponent> elements) {
        super("note");
        // support only Text and Image
        this.elements = elements.stream().filter(c -> (c instanceof Text) || (c instanceof Image)).collect(Collectors.toList());
    }

    @Override
    public Object toObjectForJson() {
        return MixUtils.newHashMap(
                "tag", getTag(),
                "elements", elements.stream().map(CardComponent::toObjectForJson).collect(Collectors.toList())
        );
    }
}
