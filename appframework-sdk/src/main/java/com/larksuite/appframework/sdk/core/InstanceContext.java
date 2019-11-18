/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core;

import com.larksuite.appframework.sdk.client.ImageKeyManager;
import com.larksuite.appframework.sdk.client.ImageKeyStorage;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.client.MiniProgramAuthenticator;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;

public class InstanceContext {

    private App app;

    private OpenApiClient openApiClient;

    private LarkAppInstance larkAppInstance;

    private TokenCenter tokenCenter;

    private LarkClient larkClient;

    private ImageKeyManager imageKeyManager;

    private MiniProgramAuthenticator miniProgramAuthenticator;

    public InstanceContext(App app, OpenApiClient openApiClient) {
        this.app = app;
        this.openApiClient = openApiClient;
        this.larkClient = new LarkClient(this, openApiClient);
    }

    public TokenCenter getTokenCenter() {
        return tokenCenter;
    }

    public void createTokenCenter(AppTicketStorage appTicketStorage) {
        this.tokenCenter = new TokenCenter(openApiClient, app, appTicketStorage);
    }

    public void createImageKeyManager(ImageKeyStorage imageKeyStorage) {
        if (imageKeyStorage != null) {
            setImageKeyManager(new ImageKeyManager(app, openApiClient, tokenCenter, imageKeyStorage));
        }
    }

    public void createMiniProgramAuthenticator(SessionManager sessionManager, Integer cookieDomainParentLevel) {
        if (sessionManager != null) {
            MiniProgramAuthenticator miniProgramAuthenticator = new MiniProgramAuthenticator(openApiClient, tokenCenter, sessionManager);
            if (cookieDomainParentLevel != null) {
                miniProgramAuthenticator.setCookieDomainParentLevel(cookieDomainParentLevel);
            }
            setMiniProgramAuthenticator(miniProgramAuthenticator);
        }
    }

    public App getApp() {
        return app;
    }

    public LarkAppInstance getLarkAppInstance() {
        return larkAppInstance;
    }

    public void setLarkAppInstance(LarkAppInstance larkAppInstance) {
        this.larkAppInstance = larkAppInstance;
    }

    public LarkClient getLarkClient() {
        return larkClient;
    }

    public void setLarkClient(LarkClient larkClient) {
        this.larkClient = larkClient;
    }

    public ImageKeyManager getImageKeyManager() {
        return imageKeyManager;
    }

    public void setImageKeyManager(ImageKeyManager imageKeyManager) {
        this.imageKeyManager = imageKeyManager;
    }

    public MiniProgramAuthenticator getMiniProgramAuthenticator() {
        return miniProgramAuthenticator;
    }

    public void setMiniProgramAuthenticator(MiniProgramAuthenticator miniProgramAuthenticator) {
        this.miniProgramAuthenticator = miniProgramAuthenticator;
    }
}
