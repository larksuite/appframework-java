package com.larksuite.appframework.sdk;

import com.larksuite.appframework.sdk.core.eventhandler.CardEventHandler;
import com.larksuite.appframework.sdk.core.eventhandler.EventCallbackHandler;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AddBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppOpenEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppStatusChangeEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppTicketEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.LeaveApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.MessageEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.OrderPaidEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.P2pChatCreateEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemedyApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.RemoveBotEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.ShiftApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.TripApprovalEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.UserAddEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.WorkApprovalEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppEventListener {

    Map<Class<? extends BaseEvent>, EventCallbackHandler> eventHandlerMap = new HashMap<>();

    CardEventHandler cardEventHandler;

    public AppEventListener onCardEvent(CardEventHandler cardEventHandler) {
        this.cardEventHandler = cardEventHandler;
        return this;
    }

    public AppEventListener onAppOpenEvent(EventCallbackHandler<AppOpenEvent> handler) {
        return onEvent(AppOpenEvent.class, handler);
    }

    public AppEventListener onApprovalEvent(EventCallbackHandler<ApprovalEvent> handler) {
        return onEvent(ApprovalEvent.class, handler);
    }

    public AppEventListener onAppStatusChangeEvent(EventCallbackHandler<AppStatusChangeEvent> handler) {
        return onEvent(AppStatusChangeEvent.class, handler);
    }

    public AppEventListener onAddBotEvent(EventCallbackHandler<AddBotEvent> handler) {
        return onEvent(AddBotEvent.class, handler);
    }

    public AppEventListener onRemoveBotEvent(EventCallbackHandler<RemoveBotEvent> handler) {
        return onEvent(RemoveBotEvent.class, handler);
    }

    public AppEventListener onUserAddEvent(EventCallbackHandler<UserAddEvent> handler) {
        return onEvent(UserAddEvent.class, handler);
    }

    public AppEventListener onLeaveApprovalEvent(EventCallbackHandler<LeaveApprovalEvent> handler) {
        return onEvent(LeaveApprovalEvent.class, handler);
    }

    public AppEventListener onMessageEvent(EventCallbackHandler<MessageEvent> handler) {
        return onEvent(MessageEvent.class, handler);
    }

    public AppEventListener onOrderPaidEvent(EventCallbackHandler<OrderPaidEvent> handler) {
        return onEvent(OrderPaidEvent.class, handler);
    }

    public AppEventListener onWorkApprovalEvent(EventCallbackHandler<WorkApprovalEvent> handler) {
        return onEvent(WorkApprovalEvent.class, handler);
    }

    public AppEventListener onP2pChatCreateEvent(EventCallbackHandler<P2pChatCreateEvent> handler) {
        return onEvent(P2pChatCreateEvent.class, handler);
    }

    public AppEventListener onRemedyApprovalEvent(EventCallbackHandler<RemedyApprovalEvent> handler) {
        return onEvent(RemedyApprovalEvent.class, handler);
    }

    public AppEventListener onShiftApprovalEvent(EventCallbackHandler<ShiftApprovalEvent> handler) {
        return onEvent(ShiftApprovalEvent.class, handler);
    }

    public AppEventListener onTripApprovalEvent(EventCallbackHandler<TripApprovalEvent> handler) {
        return onEvent(TripApprovalEvent.class, handler);
    }

    public AppEventListener onAppTicketEvent(EventCallbackHandler<AppTicketEvent> handler) {
        return onEvent(AppTicketEvent.class, handler);
    }

    public <T extends BaseEvent> AppEventListener onEvent(Class<T> clazz, EventCallbackHandler<T> handler) {
        eventHandlerMap.put(clazz, handler);
        return this;
    }

    public AppEventListener onEvents(Map<Class<? extends BaseEvent>, EventCallbackHandler> events) {
        eventHandlerMap.putAll(events);
        return this;
    }

    @Override
    public String toString() {
        List<Class> list = new ArrayList<>(eventHandlerMap.keySet());
        list.add(CardEventHandler.class);
        return list.stream().map(Class::getSimpleName).collect(Collectors.joining(","));
    }
}
