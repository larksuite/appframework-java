/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.fasterxml.jackson.core.type.TypeReference;
import com.larksuite.appframework.sdk.client.ImageKeyManager;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MiniProgramAuthenticator;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.eventhandler.AppEventHandlerManager;
import com.larksuite.appframework.sdk.core.eventhandler.AppTicketEventCallbackHandler;
import com.larksuite.appframework.sdk.core.protocol.card.CardEvent;
import com.larksuite.appframework.sdk.core.protocol.event.impl.AppTicketEvent;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import com.larksuite.appframework.sdk.core.protocol.event.CallbackEventParser;
import com.larksuite.appframework.sdk.exception.HandlerNotFoundException;
import com.larksuite.appframework.sdk.utils.LoggerUtil;
import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.sdk.utils.NamedThreadFactory;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LarkAppInstance {

    private InstanceContext instanceContext;

    private CallbackEventParser callbackEventParser = new CallbackEventParser();

    private volatile AppEventHandlerManager appEventHandlerManager;

    private ScheduledExecutorService delayResendSes = Executors.newScheduledThreadPool(1, new NamedThreadFactory("delay-resend", true));

    public LarkAppInstance(InstanceContext instanceContext) {
        this.instanceContext = instanceContext;

        this.appEventHandlerManager = createAppEventHandlerManager(null);

        this.instanceContext.setLarkAppInstance(this);
    }

    public void init() {
        this.instanceContext.getTokenCenter().ensureInit();
    }

    public LarkClient getLarkClient() {
        return instanceContext.getLarkClient();
    }

    public TokenCenter getTokenCenter() {
        return instanceContext.getTokenCenter();
    }

    public InstanceContext getInstanceContext() {
        return instanceContext;
    }

    public App getApp() {
        return instanceContext.getApp();
    }

    public MiniProgramAuthenticator getMiniProgramAuthenticator() {
        return instanceContext.getMiniProgramAuthenticator();
    }

    public String getAppShortName() {
        return instanceContext.getApp().getAppShortName();
    }

    public ImageKeyManager getImageKeyManager() {
        return instanceContext.getImageKeyManager();
    }

    /**
     * called when receive lark event notify json data
     *
     * @param notifyJsonData data from lark notify request
     * @return response data for sending back to lark synchronously
     */
    public String receiveLarkNotify(String notifyJsonData) {
        final App app = instanceContext.getApp();

        Map<String, Object> data;
        try {
            data = decrypt(notifyJsonData);
        } catch (IOException e) {
            LoggerUtil.GLOBAL_LOGGER.error("receiveLarkNotify exception", e);
            return "";
        }

        String type;
        {
            Object t = data.get("type");
            if (t == null) {
                LoggerUtil.GLOBAL_LOGGER.error("receive notify json data, but cannot find type, data: {}", notifyJsonData);
                return "";
            }
            type = t.toString();
        }


        if (type.equals("url_verification")) {
            return challenge(data);
        } else if (type.equals("event_callback")) {

            Map<String, Object> eventData = (Map<String, Object>) data.get("event");
            if (eventData == null) {
                LoggerUtil.GLOBAL_LOGGER.error("receive notify json data, but cannot find event data, data: {}", notifyJsonData);
                return "";
            }

            String eventType = (String) eventData.get("type");

            if (eventType == null) {
                LoggerUtil.GLOBAL_LOGGER.error("receive notify json data, but cannot find event type, data: {}", notifyJsonData);
                return "";
            }

            BaseEvent event = callbackEventParser.parseEvent(eventType, eventData);

            if (event == null) {
                LoggerUtil.GLOBAL_LOGGER.error("receive notify json data, but unknown eventType: {}, data: {}", eventType, notifyJsonData);
                return "";
            }

            if (!app.getAppId().equals(event.getAppId())) {
                LoggerUtil.GLOBAL_LOGGER.error("receive notify json data, but appId not matched, expect appId: {}, data: {}", app.getAppId(), notifyJsonData);
                return "";
            }


            Object r = null;
            try {
                r = appEventHandlerManager.fireEventCallback(instanceContext, event);
            } catch (HandlerNotFoundException e) {
                LoggerUtil.GLOBAL_LOGGER.warn("handler for app {} type {} not registered, event dropped." , app.getAppShortName(), eventType);
            }

            if (r == null) {
                return "";
            } else if (r instanceof String) {
                return (String) r;
            } else {
                return JsonUtil.larkFormatToJsonString(r);
            }
        }


        return "";
    }


    /**
     *
     * called when receive card notify json data
     *
     * @param notifyJsonData data from lark notify request
     * @param request
     * @return response data for sending back to lark synchronously
     */
    public String receiveCardNotify(String notifyJsonData, HttpServletRequest request) {
        final App app = instanceContext.getApp();

        try {
            Map<String, Object> data = parseNotifyJsonData(notifyJsonData);

            if ("url_verification".equals(data.get("type"))) {
                return challenge(data);
            }

            if (!verifyCardNotify(app, request, notifyJsonData)) {
                LoggerUtil.GLOBAL_LOGGER.error("verify Signature failed, appName: {}", app.getAppShortName());
                return "";
            }

            if (data.containsKey("action")) {

                CardEvent ce = JsonUtil.larkFormatConvertToJavaObject(data, CardEvent.class);

                Card resultCard = appEventHandlerManager.fireCardEvent(instanceContext, ce);

                return resultCard == null ? "" : JsonUtil.larkFormatToJsonString(resultCard.toObjectForJson());
            }

        } catch (IOException e) {
            LoggerUtil.GLOBAL_LOGGER.error("receiveCardNotify parseNotifyJsonData exception, appShortName:{}, data: {}", app.getAppShortName(), notifyJsonData);
        }

        return "";
    }

    public LarkAppInstance setAppEventListener(AppEventListener appEventListener) {
        this.appEventHandlerManager = createAppEventHandlerManager(appEventListener);
        return this;
    }

    private AppEventHandlerManager createAppEventHandlerManager(AppEventListener l) {
        AppEventHandlerManager appEventHandlerManager = new AppEventHandlerManager();

        if (l != null) {
            l.eventHandlerMap.forEach((c, h) -> appEventHandlerManager.registerEventCallbackHandler(c, h));
            appEventHandlerManager.registerCardEventHandler(l.cardEventHandler);
        }

        if (instanceContext.getApp().getIsIsv()) {
            appEventHandlerManager.registerEventCallbackHandler(AppTicketEvent.class, new AppTicketEventCallbackHandler());
        }

        return appEventHandlerManager;
    }

    private boolean verifyCardNotify(App app, HttpServletRequest request, String notifyJsonData) {
        String timestamp = request.getHeader("X-Lark-Request-Timestamp");
        String nonce = request.getHeader("X-Lark-Request-Nonce");
        String sig = request.getHeader("X-Lark-Signature");

        String token = app.getVerificationToken();
        String s = timestamp + nonce + token + notifyJsonData;


        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (Exception e) {
        }

        byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(String.format("%02X", b));
        }
        return sb.toString().toLowerCase().equals(sig);
    }

    private Map<String, Object> decrypt(String notifyJsonData) throws IOException {

        Map<String, Object> data = parseNotifyJsonData(notifyJsonData);

        if (!instanceContext.getApp().needDecrypt()) {
            return data;
        }

        Object enc = data.get("encrypt");
        if (enc == null) {
            throw new IOException("encrypt key configured but notify data not encrypted, data: " + notifyJsonData);
        }
        String decrypted;
        try {
            decrypted = instanceContext.getApp().decrypt(enc.toString());
        } catch (Exception e) {
            throw new IOException("decrypt notify data failed, encrypt string: " + enc.toString());
        }

        LoggerUtil.GLOBAL_LOGGER.debug("notify data after decrypted content: {}", decrypted);

        return parseNotifyJsonData(decrypted);
    }

    private Map<String, Object> parseNotifyJsonData(String notifyJsonData) throws IOException {
        try {
            return JsonUtil.larkFormatToObject(notifyJsonData, new TypeReference<Map<String, Object>>() {
            });
        } catch (IOException e) {
            throw new IOException("receive notify json data, parse exception, data: " + notifyJsonData, e);
        }
    }

    private String challenge(Map<String, Object> data) {
        if (instanceContext.getApp().checkVerificationToken((String) data.get("token"))) {
            if (instanceContext.getApp().getIsIsv()) {
                delayResendSes.schedule(() -> instanceContext.getTokenCenter().askForResendAppTicket(), 1000, TimeUnit.MILLISECONDS);
            }
            return JsonUtil.larkFormatToJsonString(MixUtils.newHashMap("challenge", data.get("challenge")));
        }
        return "";
    }

}
