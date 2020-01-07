/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.auth;

import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.protocol.*;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.LoggerUtil;


import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class AppTokenManager {

    private final App app;

    private final OpenApiClient openApiClient;

    private volatile AppAccessToken appAccessToken;

    /**
     * used when this app is created by ISV
     */
    private final AppTicketStorage appTicketStorage;

    private final Map<String, AppTenantAccessToken> tenantTokenMap = new ConcurrentHashMap<>();

    private final Lock appAccessTokenFetchLock = new ReentrantLock();

    private final Map<String, FutureTask<AppTenantAccessToken>> tenantTokenFetchFutureMap = new ConcurrentHashMap<>();

    public AppTokenManager(OpenApiClient openApiClient, App app, AppTicketStorage appTicketStorage) {
        this.app = app;
        this.openApiClient = openApiClient;
        this.appTicketStorage = appTicketStorage;
    }

    public void refreshAppAccessToken() {

        if (appAccessTokenFetchLock.tryLock()) {
            // we do fetch only if nobody fetching
            try {
                if (app.getIsIsv()) {
                    String appTicket = appTicketStorage.loadAppTicket(app.getAppShortName(), app.getAppId());
                    if (appTicket == null) {
                        throw new IllegalStateException("appTicket not found, maybe we should wait for ticket send");
                    }

                    FetchAppAccessTokenIsvRequest req = new FetchAppAccessTokenIsvRequest();
                    req.setAppId(app.getAppId());
                    req.setAppSecret(app.getAppSecret());
                    req.setAppTicket(appTicket);
                    FetchAppAccessTokenIsvResponse resp = openApiClient.fetchAppAccessTokenIsv(req);

                    appAccessToken = new AppAccessToken(app.getAppId(), resp.getAppAccessToken(), toExpireTime(resp.getExpire()));

                } else {
                    FetchAppAccessTokenInternalRequest req = new FetchAppAccessTokenInternalRequest();
                    req.setAppId(app.getAppId());
                    req.setAppSecret(app.getAppSecret());
                    FetchAppAccessTokenInternalResponse resp = openApiClient.fetchAppAccessTokenInternal(req);

                    appAccessToken = new AppAccessToken(app.getAppId(), resp.getAppAccessToken(), toExpireTime(resp.getExpire()));
                }
            } catch (LarkClientException e) {
                LoggerUtil.GLOBAL_LOGGER.error("refreshAppAccessToken exception, appName: {}, appId:{}", app.getAppShortName(), app.getAppId(), e);
            } finally {
                appAccessTokenFetchLock.unlock();
            }
        } else {
            // if someone is already fetching token, we do nothing but wait for fetching done
            try {
                appAccessTokenFetchLock.lock();
            } finally {
                appAccessTokenFetchLock.unlock();
            }
        }
    }

    public void refreshTenantToken(String tenantKey) throws LarkClientException {
        FutureTask<AppTenantAccessToken> doingTask = tenantTokenFetchFutureMap.get(tenantKey);

        if (doingTask == null) {

            final Callable<AppTenantAccessToken> c = () -> {

                if (app.getIsIsv()) {
                    if (appAccessToken == null || appAccessToken.isExpired()) {
                        refreshAppAccessToken();
                    }

                    if (appAccessToken == null || appAccessToken.isExpired()) {
                        return null;
                    }

                    FetchTenantAccessTokenIsvRequest req = new FetchTenantAccessTokenIsvRequest();
                    req.setTenantKey(tenantKey);
                    req.setAppAccessToken(appAccessToken.getToken());

                    FetchTenantAccessTokenIsvResponse resp = openApiClient.fetchTenantAccessTokenIsv(req);

                    return new AppTenantAccessToken(app.getAppId(), tenantKey,
                            resp.getTenantAccessToken(),
                            toExpireTime(resp.getExpire()));
                } else {
                    FetchTenantAccessTokenInternalRequest req = new FetchTenantAccessTokenInternalRequest();
                    req.setAppId(app.getAppId());
                    req.setAppSecret(app.getAppSecret());
                    FetchTenantAccessTokenInternalResponse resp = openApiClient.fetchTenantAccessTokenInternal(req);

                    return new AppTenantAccessToken(app.getAppId(), tenantKey,
                            resp.getTenantAccessToken(),
                            toExpireTime(resp.getExpire()));
                }
            };

            FutureTask<AppTenantAccessToken> newTask = new FutureTask<>(() -> {
                // retry fetch tenant access token after refresh app access token
                try {
                    return c.call();
                } catch (LarkClientException e) {
                    if (e.getCode() == LarkClientException.APP_ACCESS_TOKEN_INVALID) {
                        refreshAppAccessToken();
                        return c.call();
                    }
                    throw e;
                }
            });

            doingTask = tenantTokenFetchFutureMap.putIfAbsent(tenantKey, newTask);

            // we run this task only if we register future successfully
            if (doingTask == null) {
                newTask.run();

                try {
                    AppTenantAccessToken appTenantAccessToken = newTask.get();
                    if (appTenantAccessToken != null) {
                        LoggerUtil.GLOBAL_LOGGER.debug("token refreshed for appId: {}, tenantKey: {}", app.getAppId(), tenantKey);
                        tenantTokenMap.put(tenantKey, appTenantAccessToken);
                    }
                } catch (InterruptedException e) {
                    // wont happen here
                } catch (ExecutionException e) {
                    if (e.getCause() instanceof LarkClientException) {
                        throw (LarkClientException)e.getCause();
                    } else {
                        throw new RuntimeException(e.getCause());
                    }
                } finally {
                    tenantTokenFetchFutureMap.remove(tenantKey, newTask);
                }

                return;
            }
        }

        try {
            // others doing the task, wait for it done
            doingTask.get();
        } catch (InterruptedException e) {
            LoggerUtil.GLOBAL_LOGGER.error("refreshTenantToken InterruptedException ", e);
        } catch (ExecutionException e) {
            if (e.getCause() instanceof LarkClientException) {
                throw (LarkClientException)e.getCause();
            } else {
                throw new RuntimeException(e.getCause());
            }
        }
    }

    public AppTenantAccessToken getTenantToken(String tenantKey) {
        return this.tenantTokenMap.get(tenantKey);
    }

    public App getApp() {
        return app;
    }

    public AppAccessToken getAppAccessToken() {
        return appAccessToken;
    }

    /**
     * translate expire duration to deadline time,
     * we make token expire 10 minutes earlier than lark server
     *
     * @param expire
     * @return
     */
    private long toExpireTime(String expire) {
        return System.currentTimeMillis() + Long.parseLong(expire) * 1000 - 1000 * 60 * 10;
    }
}
