/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.client.message.card.CardActionUtils;

import java.util.HashMap;
import java.util.Map;

public abstract class ActionElement extends Element {

    private String methodName;

    private Map<String, String> value = new HashMap<>(4);

    public ActionElement(String tag, String methodName) {
        super(tag);
        this.methodName = methodName;
        CardActionUtils.setActionMethodName(value, methodName);
    }

    public String getMethodName() {
        return methodName;
    }

    public Map<String, String> getValue() {
        return value;
    }

    protected void addActionValues(Map<String, String> value) {
        this.value.putAll(value);
    }
}
