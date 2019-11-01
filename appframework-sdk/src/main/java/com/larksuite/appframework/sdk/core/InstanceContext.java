/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core;

import com.larksuite.appframework.sdk.client.ImageKeyManager;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.client.MiniProgramAuthenticator;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;

public class InstanceContext {

    private App app;

    private LarkAppInstance larkAppInstance;

    private TokenCenter tokenCenter;

    private LarkClient larkClient;

    private ImageKeyManager imageKeyManager;

    private MiniProgramAuthenticator miniProgramAuthenticator;

    public TokenCenter getTokenCenter() {
        return tokenCenter;
    }

    public void setTokenCenter(TokenCenter tokenCenter) {
        this.tokenCenter = tokenCenter;
    }

    public App getApp() {
        return app;
    }

    public void setApp(App app) {
        this.app = app;
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
