/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Confirm;
import com.larksuite.appframework.sdk.client.message.card.objects.Option;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class SelectMenu extends ActionElement {

    private Text placeholder;

    private String initialOption;

    private List<Option> options;

    private Confirm confirm;

    public SelectMenu(String tag, String actionName) {
        super(tag, actionName);
    }

    public SelectMenu setPlaceholder(Text p) {
        this.placeholder = p;
        return this;
    }

    public SelectMenu setInitialOption(String io) {
        this.initialOption = io;
        return this;
    }

    public SelectMenu setOptions(List<Option> options) {
        this.options = options;
        return this;
    }

    public SelectMenu setValue(Map<String, String> value) {
        super.addActionValues(value);
        return this;
    }

    public SelectMenu setConfirm(Confirm c) {
        this.confirm = c;
        return this;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = MixUtils.newHashMap("tag", getTag());

        if (placeholder != null) {
            r.put("placeholder", placeholder.toObjectForJson());
        }

        if (initialOption != null) {
            r.put("initial_option", initialOption);
        }

        if (options != null) {
            r.put("options", options.stream().map(Option::toObjectForJson).collect(Collectors.toList()));
        }

        r.put("value", getValue());

        if (confirm != null) {
            r.put("confirm", confirm.toObjectForJson());
        }

        return r;
    }

    public static class Static extends SelectMenu {

        public Static(String actionName) {
            super("select_static", actionName);
        }
    }

    public static class Person extends SelectMenu {
        public Person(String actionName) {
            super("select_person", actionName);
        }
    }
}
