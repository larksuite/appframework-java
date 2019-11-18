/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client.message.card;

import com.larksuite.appframework.sdk.client.message.card.module.Module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Card implements CardComponent {

    private Config config;

    private Header header;

    private List<Module> elements;

    private Map<String, List<Module>> i18nElements;

    private List<String> openIds;

    public Card(Config config, Header header) {
        this.config = config;
        this.header = header;
    }

    public void setModules(List<Module> l) {
        elements = l;
    }

    public void setZhCnModules(List<Module> l) {
        ensureI18nElements();
        i18nElements.put("zh_cn", l);
    }

    public void setEnUsModules(List<Module> l) {
        ensureI18nElements();
        i18nElements.put("en_us", l);
    }

    public void setJaJpModules(List<Module> l) {
        ensureI18nElements();
        i18nElements.put("ja_jp", l);
    }

    public void setOpenIds(List<String> ids) {
        openIds = ids;
    }

    private void ensureI18nElements() {
        if (i18nElements == null) {
            i18nElements = new HashMap<>(3);
        }
    }

    @Override
    public Object toObjectForJson() {
        Map<String, Object> r = new HashMap<>();

        if (header != null) {
            r.put("header", header.toObjectForJson());
        }

        if (config != null) {
            r.put("config", config.toObjectForJson());
        }

        if (elements != null) {
            r.put("elements", elements.stream().map(CardComponent::toObjectForJson).collect(Collectors.toList()));
        }

        if (openIds != null) {
            r.put("open_ids", openIds);
        }

        if (i18nElements != null) {
            r.put("i18nElements", i18nElements.entrySet().stream().collect(Collectors.toMap(
                    Map.Entry::getKey,
                    e -> e.getValue().stream().map(CardComponent::toObjectForJson).collect(Collectors.toList()))
            ));
        }

        return r;
    }
}
