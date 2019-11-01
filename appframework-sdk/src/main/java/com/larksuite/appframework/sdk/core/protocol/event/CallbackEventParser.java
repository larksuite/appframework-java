/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event;

import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddUserToChatEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppOpenEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppStatusChangeEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppTicketEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ChatDisbandEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ContactScopeChangeEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.CreateWidgetInstanceEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.DeleteWidgetInstanceEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.DeptAddEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.DeptDeleteEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.DeptUpdateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.GroupSettingUpdateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.LeaveApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.MessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.OrderPaidEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.P2pChatCreateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemedyApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemoveBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemoveUserFromChatEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RevokeAddUserFromChatEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ShiftApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.TripApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.UserAddEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.UserLeaveEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.UserUpdateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.WorkApprovalEvent;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class CallbackEventParser {

    private static Map<String, Class<? extends BaseEvent>> eventTypeMap;

    static {
        eventTypeMap = new HashMap<>(32);
        registerEventType(AppOpenEvent.class);
        registerEventType(ApprovalEvent.class);
        registerEventType(AppStatusChangeEvent.class);
        registerEventType(AddBotEvent.class);
        registerEventType(RemoveBotEvent.class);

        registerEventType(UserAddEvent.class);
        registerEventType(UserUpdateEvent.class);
        registerEventType(UserLeaveEvent.class);

        registerEventType(DeptAddEvent.class);
        registerEventType(DeptUpdateEvent.class);
        registerEventType(DeptDeleteEvent.class);

        registerEventType(ContactScopeChangeEvent.class);

        registerEventType(LeaveApprovalEvent.class);
        registerEventType(MessageEvent.class);
        registerEventType(OrderPaidEvent.class);
        registerEventType(WorkApprovalEvent.class);
        registerEventType(P2pChatCreateEvent.class);
        registerEventType(RemedyApprovalEvent.class);
        registerEventType(ShiftApprovalEvent.class);
        registerEventType(TripApprovalEvent.class);
        registerEventType(AppTicketEvent.class);
        registerEventType(AddUserToChatEvent.class);
        registerEventType(RemoveUserFromChatEvent.class);
        registerEventType(RevokeAddUserFromChatEvent.class);
        registerEventType(ChatDisbandEvent.class);
        registerEventType(GroupSettingUpdateEvent.class);

        registerEventType(CreateWidgetInstanceEvent.class);
        registerEventType(DeleteWidgetInstanceEvent.class);
    }

    private static void registerEventType(Class<? extends BaseEvent> eventClass) {
        Event annotation = eventClass.getAnnotation(Event.class);
        if (annotation == null) {
            return;
        }
        eventTypeMap.put(annotation.type(), eventClass);
    }

    public BaseEvent parseEvent(String type, Object data) {
        Class<? extends BaseEvent> clazz = eventTypeMap.get(type);
        if (clazz == null) {
            return null;
        }
        return JsonUtil.larkFormatConvertToJavaObject(data, clazz);
    }
}
