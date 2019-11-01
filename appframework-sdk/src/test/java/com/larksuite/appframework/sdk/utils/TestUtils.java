/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import com.fasterxml.jackson.core.type.TypeReference;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class TestUtils {

    public static boolean jsonEquals(String s1, String s2) {
        try {
            Map<String, Object> o1 = JsonUtil.toObject(s1, new TypeReference<Map<String, Object>>() {});
            Map<String, Object> o2 = JsonUtil.toObject(s2, new TypeReference<Map<String, Object>>() {});

            return equals(o1, o2);

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean objectEqualsJson(Object o, String json) {
        String s1 = JsonUtil.toJsonString(o);
        String s2 = json;

        return jsonEquals(s1, s2);
    }

    @SuppressWarnings("unchecked")
    public static boolean equals(Map<String, Object> o1, Map<String, Object> o2) {
        if (o1 == null) {
            return o2 == null;
        }

        if (o2 == null){
            return false;
        }

        if (o1.size() != o2.size()) {
            return false;
        }

        for (Map.Entry<String, Object> e : o1.entrySet()) {

            Object v1 = e.getValue();
            Object v2 = o2.get(e.getKey());

            if (v1 == null || v2 == null) {
                return v1 == null && v2 == null;
            }

            if (v1.getClass() != v2.getClass()) {
                return false;
            }

            if (v1 instanceof Map) {

                if (!equals((Map<String, Object>)v1, (Map<String, Object>)v2)) {
                    return false;
                }
            } else if (!v1.equals(v2)) {
                return false;
            }
        }

        return true;
    }

    public static String loadJsonFile(String resource) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            MixUtils.copyStream(ClassLoader.getSystemResourceAsStream(resource), bos);

            return new String(bos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
