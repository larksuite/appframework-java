/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card.element;

import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.client.message.card.objects.Confirm;
import com.larksuite.appframework.sdk.client.message.card.objects.Text;

import java.util.Map;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class DatePicker extends ActionElement {

    private Text placeholder;

    private String initialDate;

    private Confirm confirm;

    public DatePicker(String methodName) {
        super("date_picker", methodName);
    }

    public DatePicker setPlaceholder(Text placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public DatePicker setInitialDate(String initialDate) {
        this.initialDate = initialDate;
        return this;
    }

    public DatePicker setValue(Map<String, String> value) {
        super.addActionValues(value);
        return this;
    }

    public DatePicker setConfirm(Confirm confirm) {
        this.confirm = confirm;
        return this;
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = MixUtils.newHashMap("tag", getTag());

        if (placeholder != null) {
            r.put("placeholder", placeholder.toObjectForJson());
        }

        if (initialDate != null) {
            r.put("initial_date", initialDate);
        }

        r.put("value", getValue());

        if (confirm != null) {
            r.put("confirm", confirm.toObjectForJson());
        }

        return r;
    }
}
