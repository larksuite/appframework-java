/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.auth;

import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.LoggerUtil;
import com.larksuite.appframework.sdk.core.protocol.ResendAppTicketRequest;

public class TokenCenter {

    private static final String INTERNAL_TENANT_KEY = "_";

    private AppTicketStorage appTicketStorage;

    private OpenApiClient openApiClient;

    private AppTokenManager appTokenManager;

    public TokenCenter(OpenApiClient openApiClient, App app, AppTicketStorage appTicketStorage) {
        this.openApiClient = openApiClient;
        this.appTicketStorage = appTicketStorage;
        this.appTokenManager = new AppTokenManager(openApiClient, app, appTicketStorage);
    }

    public void ensureInit() {
        if (appTokenManager.getApp().getIsIsv()) {
            if (appTicketStorage == null) {
                throw new IllegalStateException("ISV app appTicketStorage could not be null");
            }
            askForResendAppTicket();
        }
    }

    public String getAppAccessToken() {

        AppAccessToken appAccessToken = appTokenManager.getAppAccessToken();
        if (appAccessToken != null && !appAccessToken.isExpired()) {
            return appAccessToken.getToken();
        } else {
            refreshAppAccessToken();
            AppAccessToken t = appTokenManager.getAppAccessToken();
            return t == null ? null : t.getToken();
        }
    }

    public String getTenantAccessToken(String tenantKey) throws LarkClientException {
        if (!appTokenManager.getApp().getIsIsv()) {
            tenantKey = INTERNAL_TENANT_KEY;
        }

        AppTenantAccessToken tenantToken = appTokenManager.getTenantToken(tenantKey);

        if (tenantToken != null && !tenantToken.isExpired()) {
            return tenantToken.getToken();
        } else {
            appTokenManager.refreshTenantToken(tenantKey);
            AppTenantAccessToken t = appTokenManager.getTenantToken(tenantKey);
            return t == null ? null : t.getToken();
        }
    }

    public void refreshAppAccessToken() {
        appTokenManager.refreshAppAccessToken();
    }

    public void refreshTenantToken(String tenantKey) throws LarkClientException {
        appTokenManager.refreshTenantToken(tenantKey);
    }

    public void askForResendAppTicket() {

        App appConfig = appTokenManager.getApp();

        ResendAppTicketRequest req = new ResendAppTicketRequest();
        req.setAppId(appConfig.getAppId());
        req.setAppSecret(appConfig.getAppSecret());

        try {
            openApiClient.resendAppTicket(req);
        } catch (LarkClientException e) {
            LoggerUtil.GLOBAL_LOGGER.error("askForResendAppTicket exception, appId: {}", appConfig.getAppId(), e);
        }
    }

    public void refreshAppTicket(String appTicket) {
        App app = appTokenManager.getApp();
        appTicketStorage.updateAppTicket(app.getAppShortName(), app.getAppId(), appTicket);
    }

    public App getApp() {
        return this.appTokenManager.getApp();
    }
}
