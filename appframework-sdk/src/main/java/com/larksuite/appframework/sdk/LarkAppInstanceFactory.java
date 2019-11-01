/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.google.common.annotations.VisibleForTesting;
import com.larksuite.appframework.sdk.client.ImageKeyManager;
import com.larksuite.appframework.sdk.client.ImageKeyStorage;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MiniProgramAuthenticator;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.eventhandler.AppEventHandlerManager;
import com.larksuite.appframework.sdk.core.eventhandler.CardEventHandler;
import com.larksuite.appframework.sdk.core.eventhandler.EventCallbackHandler;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.core.protocol.event.*;
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
import com.larksuite.appframework.sdk.utils.HttpClient;
import com.larksuite.appframework.sdk.utils.SimpleHttpClient;

import java.util.HashMap;
import java.util.Map;

public class LarkAppInstanceFactory {

    public static AppEventListener createAppEventCallbackListener() {
        return new AppEventListener();
    }

    public static LarkAppInstanceBuilder builder(AppConfiguration c) {
        LarkAppInstanceBuilder b = new LarkAppInstanceBuilder();
        b.configuration = c;
        return b;
    }

    public static class LarkAppInstanceBuilder {

        private HttpClient httpClient;

        private AppConfiguration configuration;

        private AppTicketStorage appTicketStorage;

        private boolean isFeishu = false;

        private AppEventListener eventListener;

        private ImageKeyStorage imageKeyStorage;

        private SessionManager sessionManager;

        private Integer cookieDomainParentLevel;

        private String basePath;

        public LarkAppInstanceBuilder feishu() {
            isFeishu = true;
            return this;
        }

        public LarkAppInstanceBuilder imageKeyStorage(ImageKeyStorage imageKeyStorage) {
            this.imageKeyStorage = imageKeyStorage;
            return this;
        }

        public LarkAppInstanceBuilder httpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
            return this;
        }

        public LarkAppInstanceBuilder appTicketStorage(AppTicketStorage appTicketStorage) {
            this.appTicketStorage = appTicketStorage;
            return this;
        }

        public LarkAppInstanceBuilder authenticator(SessionManager sm, Integer cookieDomainParentLevel) {
            this.sessionManager = sm;
            this.cookieDomainParentLevel = cookieDomainParentLevel;
            return this;
        }

        public LarkAppInstanceBuilder registerAppEventCallbackListener(AppEventListener listener) {
            this.eventListener = listener;
            return this;
        }

        @VisibleForTesting
        LarkAppInstanceBuilder mockLarkOpenPlatformBasePath(String basePath) {
            this.basePath = basePath;
            return this;
        }

        public LarkAppInstance create() {

            if (httpClient == null) {
                httpClient = new SimpleHttpClient();
            }

            OpenApiClient openApiClient;

            if (basePath != null) {
                openApiClient = new OpenApiClient(httpClient, basePath);
            } else {
                openApiClient = new OpenApiClient(httpClient, isFeishu);
            }

            AppConfiguration.checkConfiguration(configuration);

            final App app = new App(configuration);

            if (app.getIsIsv() && appTicketStorage == null) {
                throw new IllegalArgumentException("AppTicketStorage should not be null for ISV app");
            }

            InstanceContext instanceContext = new InstanceContext();
            instanceContext.setApp(app);
            instanceContext.setTokenCenter(new TokenCenter(openApiClient, instanceContext.getApp(), appTicketStorage));

            // lark client
            new LarkClient(instanceContext, openApiClient);

            // image key storage
            if (imageKeyStorage != null) {
                instanceContext.setImageKeyManager(new ImageKeyManager(app, openApiClient, instanceContext.getTokenCenter(), imageKeyStorage));
            }

            // mini program authenticator
            if (sessionManager != null) {
                MiniProgramAuthenticator miniProgramAuthenticator = new MiniProgramAuthenticator(openApiClient, instanceContext.getTokenCenter(), sessionManager);
                if (cookieDomainParentLevel != null) {
                    miniProgramAuthenticator.setCookieDomainParentLevel(cookieDomainParentLevel);
                }
                instanceContext.setMiniProgramAuthenticator(miniProgramAuthenticator);
            }

            // event handlers
            AppEventHandlerManager appEventHandlerManager = new AppEventHandlerManager();
            eventListener.eventHandlerMap.forEach((c, h) -> appEventHandlerManager.registerEventCallbackHandler(c, h));
            appEventHandlerManager.registerCardEventHandler(eventListener.cardEventHandler);

            return new LarkAppInstance(instanceContext, appEventHandlerManager);
        }
    }

    public static class AppEventListener {

        private Map<Class<? extends BaseEvent>, EventCallbackHandler> eventHandlerMap = new HashMap<>();

        private CardEventHandler cardEventHandler;

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
    }

}
