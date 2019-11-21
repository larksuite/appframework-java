/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol.event;

import com.larksuite.appframework.sdk.annotation.Event;
import com.larksuite.appframework.sdk.annotation.MessageEvent;
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
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.BaseMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.FileMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.ImageMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.MergeForwardMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.PostMessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.message.TextMessageEvent;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import java.util.HashMap;
import java.util.Map;

public class CallbackEventParser {

    private static Map<String, EventParser> eventTypeMap;

    static {
        eventTypeMap = new HashMap<>(32);
        registerClassMappingEventTypeParser(AppOpenEvent.class);
        registerClassMappingEventTypeParser(ApprovalEvent.class);
        registerClassMappingEventTypeParser(AppStatusChangeEvent.class);
        registerClassMappingEventTypeParser(AddBotEvent.class);
        registerClassMappingEventTypeParser(RemoveBotEvent.class);

        registerClassMappingEventTypeParser(UserAddEvent.class);
        registerClassMappingEventTypeParser(UserUpdateEvent.class);
        registerClassMappingEventTypeParser(UserLeaveEvent.class);

        registerClassMappingEventTypeParser(DeptAddEvent.class);
        registerClassMappingEventTypeParser(DeptUpdateEvent.class);
        registerClassMappingEventTypeParser(DeptDeleteEvent.class);

        registerClassMappingEventTypeParser(ContactScopeChangeEvent.class);

        registerClassMappingEventTypeParser(LeaveApprovalEvent.class);
        registerClassMappingEventTypeParser(OrderPaidEvent.class);
        registerClassMappingEventTypeParser(WorkApprovalEvent.class);
        registerClassMappingEventTypeParser(P2pChatCreateEvent.class);
        registerClassMappingEventTypeParser(RemedyApprovalEvent.class);
        registerClassMappingEventTypeParser(ShiftApprovalEvent.class);
        registerClassMappingEventTypeParser(TripApprovalEvent.class);
        registerClassMappingEventTypeParser(AppTicketEvent.class);
        registerClassMappingEventTypeParser(AddUserToChatEvent.class);
        registerClassMappingEventTypeParser(RemoveUserFromChatEvent.class);
        registerClassMappingEventTypeParser(RevokeAddUserFromChatEvent.class);
        registerClassMappingEventTypeParser(ChatDisbandEvent.class);
        registerClassMappingEventTypeParser(GroupSettingUpdateEvent.class);

        registerClassMappingEventTypeParser(CreateWidgetInstanceEvent.class);
        registerClassMappingEventTypeParser(DeleteWidgetInstanceEvent.class);

        // message event parser
        {
            MessageEventParser parser = new MessageEventParser();
            parser.registerTypes(
                    TextMessageEvent.class,
                    PostMessageEvent.class,
                    ImageMessageEvent.class,
                    MergeForwardMessageEvent.class,
                    FileMessageEvent.class
            );
            registerEventParser(BaseMessageEvent.class.getAnnotation(Event.class), parser);
        }
    }

    private static void registerClassMappingEventTypeParser(Class<? extends BaseEvent> eventClass) {
        Event annotation = eventClass.getAnnotation(Event.class);
        registerEventParser(annotation, new ClassMappingEventParser(eventClass));
    }

    private static void registerEventParser(Event eventAnn, EventParser eventParser) {
        if (eventAnn != null) {
            eventTypeMap.put(eventAnn.type(), eventParser);
        }
    }

    public BaseEvent parseEvent(String type, Map<String, Object> data) {

        EventParser parser = eventTypeMap.get(type);
        if (parser == null) {
            return null;
        }
        return parser.parse(data);
    }

    interface EventParser {
        BaseEvent parse(Map<String, Object> data);
    }

    private static class ClassMappingEventParser implements EventParser {

        Class<? extends BaseEvent> clazz;

        ClassMappingEventParser(Class<? extends BaseEvent> clazz) {
            this.clazz = clazz;
        }

        @Override
        public BaseEvent parse(Map<String, Object> data) {
            return JsonUtil.larkFormatConvertToJavaObject(data, clazz);
        }
    }

    private static class MessageEventParser implements EventParser {

        private static Map<String, Class<? extends BaseMessageEvent>> messageEventTypeMap = new HashMap<>();

        public void registerTypes(Class<? extends BaseMessageEvent> ...clz) {
            for (Class<? extends BaseMessageEvent> aClass : clz) {
                MessageEvent ann = aClass.getAnnotation(MessageEvent.class);
                if (ann == null) {
                    return;
                }
                messageEventTypeMap.put(ann.type(), aClass);
            }
        }

        @Override
        public BaseEvent parse(Map<String, Object> data) {

            String msgType = data.get("msg_type").toString();
            if (msgType == null) {
                return null;
            }

            Class<? extends BaseMessageEvent> msgEventClz = messageEventTypeMap.get(msgType);
            if (msgEventClz == null) {
                return null;
            }

            return JsonUtil.larkFormatConvertToJavaObject(data, msgEventClz);
        }
    }
}
