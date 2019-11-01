/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.utils.MockHttpServletResponse;
import com.larksuite.appframework.sdk.utils.MockOpenApiClient;
import com.larksuite.appframework.sdk.client.AbstractSessionManager;
import com.larksuite.appframework.sdk.client.MiniProgramAuthenticator;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.MockAppTicketStorage;
import com.larksuite.appframework.sdk.utils.MockHttpServletRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.larksuite.appframework.sdk.utils.Constants.*;
import static org.junit.jupiter.api.Assertions.*;


public class MiniProgramAuthenticatorTest {

    static MiniProgramAuthenticator miniProgramAuthenticator;

    @BeforeAll
    public static void buildClient() {

        OpenApiClient openApiClient = new MockOpenApiClient(null, null);

        App app = new App(APP_NAME, APP_ID, APP_SECRET, null, null, true);

        TokenCenter tokenCenter = new TokenCenter(openApiClient, app, new MockAppTicketStorage());
        tokenCenter.refreshAppTicket(APP_TICKET);

        SessionManager sessionManager = new AbstractSessionManager() {

            private Map<String, String> sessionMap = new ConcurrentHashMap<>();

            @Override
            protected String loadSessionData(String sessionId) {
                return sessionMap.get(sessionId);
            }

            @Override
            protected void persistSessionData(String sessionId, String sessionData, int validPeriod) {
                sessionMap.put(sessionId, sessionData);
            }
        };

        sessionManager.turnOnEncryption("123");

        miniProgramAuthenticator = new MiniProgramAuthenticator(openApiClient, tokenCenter, sessionManager);
        miniProgramAuthenticator.setCookieDomainParentLevel(1);


    }


    @Test
    public void testAllThrough() throws LarkClientException {

        final Cookie cookie;
        {
            HttpRequestWithAddCookie loginRequest = new HttpRequestWithAddCookie();
            HttpResponseWithGetCookie loginResponse = new HttpResponseWithGetCookie();

            miniProgramAuthenticator.login(CODE, loginRequest, loginResponse);

            cookie = loginResponse.getCookies().get(0);

            assertEquals("bframewk-session-xxx", cookie.getName());
            assertEquals("b.c.com", cookie.getDomain());
        }

        HttpRequestWithAddCookie request = new HttpRequestWithAddCookie();
        request.addCookie(cookie);

        {
            SessionManager.SessionInfo session = miniProgramAuthenticator.getSession(request);

            assertNotNull(session);

            assertEquals(session.getOpenId(), "openId");
            assertEquals(session.getEmployeeId(), "employeeId");
        }

        HttpResponseWithGetCookie logoutResponse = new HttpResponseWithGetCookie();

        miniProgramAuthenticator.logout(request, logoutResponse);

        Cookie logoutCookie = logoutResponse.getCookies().get(0);


        assertEquals("b.c.com", logoutCookie.getDomain());
        assertEquals(0, logoutCookie.getMaxAge());
        assertEquals("bframewk-session-xxx", logoutCookie.getName());
    }

    static class HttpRequestWithAddCookie extends MockHttpServletRequest {

        private List<Cookie> list = new ArrayList<>();

        @Override
        public String getServerName() {
            return "a.b.c.com";
        }

        @Override
        public Cookie[] getCookies() {
            return list.toArray(new Cookie[0]);
        }

        public void addCookie(Cookie cookie) {
            list.add(cookie);
        }
    }

    static class HttpResponseWithGetCookie extends MockHttpServletResponse {

        private List<Cookie> list = new ArrayList<>();

        public List<Cookie> getCookies() {
            return list;
        }

        @Override
        public void addCookie(Cookie cookie) {
            list.add(cookie);
        }
    }
}
