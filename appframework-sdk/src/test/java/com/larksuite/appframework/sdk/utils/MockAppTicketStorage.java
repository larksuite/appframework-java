/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MockAppTicketStorage implements AppTicketStorage {

    Map<String, String> storage = new ConcurrentHashMap<>();
    @Override
    public void updateAppTicket(String appShortName, String appId, String appTicket) {
        storage.put(appId, appTicket);
    }

    @Override
    public String loadAppTicket(String appShortName, String appId) {
        return storage.get(appId);
    }
}
