/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.protocol.MiniProgramLoginRequest;
import com.larksuite.appframework.sdk.core.protocol.MiniProgramLoginResponse;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Optional;

public class MiniProgramAuthenticator {

    private TokenCenter tokenCenter;

    private SessionManager sessionManager;

    private OpenApiClient openApiClient;

    private App app;

    private int level = 0;

    public MiniProgramAuthenticator(OpenApiClient openApiClient, TokenCenter tokenCenter, SessionManager sessionManager) {
        this.openApiClient = openApiClient;
        this.tokenCenter = tokenCenter;
        this.sessionManager = sessionManager;
        this.app = this.tokenCenter.getApp();
    }

    public void setCookieDomainParentLevel(int level) {
        this.level = level;
    }

    public void login(String code, HttpServletRequest request, HttpServletResponse response) throws LarkClientException {

        MiniProgramLoginRequest req = new MiniProgramLoginRequest();
        req.setCode(code);

        MiniProgramLoginResponse resp = openApiClient.miniProgramLoginValidate(tokenCenter.getAppAccessToken(), req);

        SessionManager.SessionInfo si = new SessionManager.SessionInfo();
        si.setAccessToken(resp.getData().getAccessToken());
        si.setEmployeeId(resp.getData().getEmployeeId());
        si.setExpiresIn(resp.getData().getExpiresIn());
        si.setOpenId(resp.getData().getOpenId());
        si.setRefreshToken(resp.getData().getRefreshToken());
        si.setSessionKey(resp.getData().getSessionKey());
        si.setTenantKey(resp.getData().getTenantKey());
        si.setUid(resp.getData().getUid());
        si.setUnionId(resp.getData().getUnionId());

        String sessionId = sessionManager.saveSession(si);

        String cookieName = sessionManager.sessionIdCookieName(app.getAppId());

        Cookie cookie = new Cookie(cookieName, sessionId);
        cookie.setPath("/");
        cookie.setMaxAge(sessionManager.getSessionMaxAge());
        cookie.setDomain(parentDomain(getDomain(request), level));

        response.addCookie(cookie);
    }

    public SessionManager.SessionInfo getSession(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        final String cookieName = sessionManager.sessionIdCookieName(app.getAppId());

        Optional<Cookie> op = Arrays.stream(cookies).filter(c -> cookieName.equals(c.getName())).findFirst();
        return op.map(cookie -> sessionManager.getSession(cookie.getValue())).orElse(null);

    }

    public boolean logout(HttpServletRequest request, HttpServletResponse response) {

        if (null == getSession(request)) {
            return false;
        }

        String cookieName = sessionManager.sessionIdCookieName(app.getAppId());

        Cookie cookie = new Cookie(cookieName, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        cookie.setDomain(parentDomain(getDomain(request), level));
        response.addCookie(cookie);
        return true;
    }


    private static String getDomain(HttpServletRequest request) {
        return request.getServerName();
    }

    private static String parentDomain(String requestUrlDomain, int level) {
        if (level <= 0) {
            return requestUrlDomain;
        }

        String[] p = requestUrlDomain.split("\\.");
        if (p.length <= 2) {
            return requestUrlDomain;
        }

        int left = p.length - level;
        if (left < 2) {
            left = 2;
        }

        String[] parentDomainParts = new String[left];
        System.arraycopy(p, p.length - left, parentDomainParts, 0, left);

        return String.join(".", parentDomainParts);
    }
}
