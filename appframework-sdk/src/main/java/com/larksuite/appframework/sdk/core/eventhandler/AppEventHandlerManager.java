/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.eventhandler;

import com.larksuite.appframework.sdk.core.protocol.card.CardEvent;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import com.larksuite.appframework.sdk.exception.HandlerNotFoundException;

import java.util.HashMap;
import java.util.Map;

public class AppEventHandlerManager {

    private Map<Class<? extends BaseEvent>, EventCallbackHandler> eventHandlerMap = new HashMap<>();

    private CardEventHandler cardEventHandler;

    public Object fireEventCallback(InstanceContext c, BaseEvent e) {

        Class<?> clazz = e.getClass();
        EventCallbackHandler callbackHandler = null;

        while (callbackHandler == null && BaseEvent.class.isAssignableFrom(clazz)) {
            callbackHandler = eventHandlerMap.get(clazz);
            clazz = clazz.getSuperclass();
        }

        if (callbackHandler == null) {
            throw new HandlerNotFoundException("cannot find handle for type " + e.getClass().getName());
        }

        return callbackHandler.handler(c, e);
    }

    public <T extends BaseEvent> void registerEventCallbackHandler(Class<T> eClazz, EventCallbackHandler<T> h) {
        if (eClazz != null && h != null) {
            eventHandlerMap.put(eClazz, h);
        }
    }

    public void registerCardEventHandler(CardEventHandler h) {
        if (h != null) {
            cardEventHandler = h;
        }
    }

    public Card fireCardEvent(InstanceContext c, CardEvent e) {
        return cardEventHandler != null ? cardEventHandler.handle(c, e) : null;
    }
}
