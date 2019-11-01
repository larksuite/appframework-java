/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper defaultObjectMapper = new ObjectMapper();

    private static final ObjectMapper larkJsonFormatObjectMapper = new ObjectMapper();

    static {
        defaultObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        larkJsonFormatObjectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        larkJsonFormatObjectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    }

    public static String toJsonString(Object o) {
        try {
            return defaultObjectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            // wont happen, ignore the exception, make interface simpler
            return null;
        }
    }

    public static <T> T toJavaObject(String s, Class<T> c) throws IOException {
        return defaultObjectMapper.readValue(s, c);
    }

    public static <T> T toObject(String s, TypeReference<T> r) throws IOException {
        return defaultObjectMapper.readValue(s, r);
    }

    public static <T> T convertToJavaObject(Object o, Class<T> c) {
        return defaultObjectMapper.convertValue(o, c);
    }


    public static String larkFormatToJsonString(Object o) {
        try {
            return larkJsonFormatObjectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            // wont happen, ignore the exception, make interface simpler
            return null;
        }
    }

    public static <T> T larkFormatToJavaObject(String s, Class<T> c) throws IOException {
        return larkJsonFormatObjectMapper.readValue(s, c);
    }

    public static <T> T larkFormatConvertToJavaObject(Object o, Class<T> c) {
        return larkJsonFormatObjectMapper.convertValue(o, c);
    }

    public static <T> T larkFormatToObject(String s, TypeReference<T> r) throws IOException {
        return larkJsonFormatObjectMapper.readValue(s, r);
    }
}