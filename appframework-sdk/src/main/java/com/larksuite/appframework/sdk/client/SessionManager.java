/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public interface SessionManager {

    String sessionIdCookieName(String appId);

    /**
     * call this method to turn on session info encryption with AES algorithm
     * @param encryptKey
     * @return
     */
    void turnOnEncryption(String encryptKey);

    /**
     * persist session info
     * @return sessionId
     */
    String saveSession(SessionInfo si);

    SessionInfo getSession(String sessionId);

    int getSessionMaxAge();

    @Setter
    @Getter
    @ToString
    class SessionInfo {

        /**
         * Token info
         */
        private String accessToken;
        private String tokenType;
        private String expiresIn;
        private String refreshToken;


        /**
         * User info
         */
        private String tenantKey;
        private String openId;
        private String employeeId;


        /**
         * Extra info
         */
        private String uid;
        private String unionId;
        private String sessionKey;

    }
}
