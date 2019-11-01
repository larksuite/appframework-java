/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import com.google.common.collect.Lists;

import java.util.List;

public class Constants {


    public static String APP_NAME = "appName";
    public static String APP_ID = "cli_xxx";
    public static String APP_SECRET = "appSecret";
    public static String CHART_ID = "chatId";
    public static String TENANT_KEY = "tenantKey";
    public static String MESSAGE_ID = "messageId";
    public static String APP_ACCESS_TOKEN = "appAccessToken";
    public static String TENANT_ACCESS_TOKEN = "tenantAccessToken";
    public static String APP_TICKET = "appTicket";

    public static String CODE = "CODE";

    public static List<String > OPEN_IDS = Lists.newArrayList("openId");
    public static List<String > USER_IDS = Lists.newArrayList("userId");
    public static List<String > DEPARTMENT_IDS = Lists.newArrayList("departmentId");

    public static List<String > INVALID_OPEN_IDS = Lists.newArrayList("invalidOpenId");
    public static List<String > INVALID_USER_IDS = Lists.newArrayList("invalidUserId");
    public static List<String > INVALID_DEPARTMENT_IDS = Lists.newArrayList("invalidDepartmentId");
    public static byte[] IMAGE_CONTENT = new byte[]{1, 2, 3, 4};

}
