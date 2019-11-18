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
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.utils.HttpClient;
import com.larksuite.appframework.sdk.utils.SimpleHttpClient;


public class LarkAppInstanceFactory {

    public static AppEventListener createAppEventListener() {
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

            LarkAppInstance ins = new LarkAppInstance(instanceContext);
            ins.setAppEventListener(eventListener);
            return ins;
        }
    }

}
