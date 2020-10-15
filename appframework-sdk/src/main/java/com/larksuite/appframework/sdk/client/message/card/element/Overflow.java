/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Confirm;
import com.larksuite.appframework.sdk.client.message.card.objects.Option;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Overflow extends ActionElement {

    private List<Option> options;

    private Confirm confirm;

    public Overflow(String methodName, List<Option> options) {
        super("overflow", methodName);
        this.options = options;
    }

    public Overflow setValue(Map<String, String> value) {
        super.addActionValues(value);
        return this;
    }

    public Overflow setConfirm(Confirm confirm) {
        this.confirm = confirm;
        return this;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = MixUtils.newHashMap(
                "tag", getTag(),
                "options", options.stream().map(Option::toObjectForJson).collect(Collectors.toList())
        );

        r.put("value", getValue());

        if (confirm != null) {
            r.put("confirm", confirm.toObjectForJson());
        }

        return r;
    }
}
