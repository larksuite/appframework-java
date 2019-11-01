/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MixUtils {

    public static boolean isBlankString(String s) {
        return (s == null || s.trim().isEmpty());
    }

    public static boolean isEmptyCollection(Collection<?> c) {
        return (c == null || c.isEmpty());
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = is.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
    }

    public static <K, V> Map<K, V> newHashMap(K k1, V v1) {
        Map<K, V> m = new HashMap<>(8);
        m.put(k1, v1);
        return m;
    }

    public static <K, V> Map<K, V> newHashMap(K k1, V v1, K k2, V v2) {
        Map<K, V> m = new HashMap<>(8);
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }

    public static <K, V> Map<K, V> newHashMap(K k1, V v1, K k2, V v2, K k3, V v3) {
        Map<K, V> m = new HashMap<>(8);
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        return m;
    }

    public static <K, V> Map<K, V> newHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4) {
        Map<K, V> m = new HashMap<>(8);
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        return m;
    }

    public static <K, V> Map<K, V> newHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5) {
        Map<K, V> m = new HashMap<>(8);
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        m.put(k5, v5);
        return m;
    }

    public static <K, V> Map<K, V> newHashMap(K k1, V v1, K k2, V v2, K k3, V v3, K k4, V v4, K k5, V v5, K k6, V v6) {
        Map<K, V> m = new HashMap<>(8);
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        m.put(k4, v4);
        m.put(k5, v5);
        m.put(k6, v6);
        return m;
    }

    public static byte[] fileToBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            copyStream(fis, bos);
            return bos.toByteArray();
        }
    }

    public static void bytesToFile(File file, byte[] bytes) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file);
             InputStream bis = new ByteArrayInputStream(bytes)) {

            copyStream(bis, fos);
        }
    }
}
