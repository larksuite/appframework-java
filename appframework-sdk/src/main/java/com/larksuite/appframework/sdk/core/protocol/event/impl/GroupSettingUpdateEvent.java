/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event.impl;


import com.larksuite.appframework.sdk.core.protocol.common.User;
import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Event(type = "group_setting_update")
public class GroupSettingUpdateEvent extends BaseEvent {

    private User operator;

    private String chatId;

    private Status afterChange;

    private Status beforeChange;

    @Setter
    @Getter
    @ToString
    public static class Status {

        private Boolean messageNotification;

        private String addMemberPermission;

        private String ownerOpenId;

        private String ownerUserId;
    }
}
