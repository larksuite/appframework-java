/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import com.larksuite.appframework.sdk.utils.JsonUtil;
import com.larksuite.appframework.sdk.utils.LoggerUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.UUID;

public abstract class AbstractSessionManager implements SessionManager {

    private SecretKey secKey;

    @Override
    public String sessionIdCookieName(String appId) {
        String prefix = "cli_";
        if (appId.startsWith(prefix)) {
            appId = appId.substring(prefix.length());
        }
        return "bframewk-session-" + appId;
    }

    @Override
    public void turnOnEncryption(String encryptKey) {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);
            this.secKey = generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            // Wont happen
        }
    }

    @Override
    public String saveSession(SessionInfo si) {

        String str = JsonUtil.toJsonString(si);

        if (secKey != null) {
            try {
                Cipher aesCipher = Cipher.getInstance("AES");
                aesCipher.init(Cipher.ENCRYPT_MODE, secKey);

                str = Base64.getEncoder().encodeToString(aesCipher.doFinal(str.getBytes(StandardCharsets.UTF_8)));

            } catch (Exception e) {
                LoggerUtil.GLOBAL_LOGGER.error("getSession exception", e);
            }
        }

        final String sessionId  = generateRandomSessionId();
        persistSessionData(sessionId, str, getSessionMaxAge());
        return sessionId;
    }

    @Override
    public SessionInfo getSession(String sessionId) {

        String sd = loadSessionData(sessionId);
        if (sd == null) {
            return null;
        }

        if (secKey != null) {
            try {
                byte[] decode = Base64.getDecoder().decode(sd);

                Cipher aesCipher = Cipher.getInstance("AES");
                aesCipher.init(Cipher.DECRYPT_MODE, secKey);

                sd = new String(aesCipher.doFinal(decode), StandardCharsets.UTF_8);
            } catch (Exception e) {
                LoggerUtil.GLOBAL_LOGGER.error("getSession exception", e);
            }
        }

        try {
            return JsonUtil.toJavaObject(sd, SessionInfo.class);
        } catch (IOException e) {
            LoggerUtil.GLOBAL_LOGGER.error("getSession parse json exception", e);
            return null;
        }
    }

    protected abstract String loadSessionData(String sessionId);

    protected abstract void persistSessionData(String sessionId, String sessionData, int validPeriod);

    /**
     * Default 7 days
     */
    @Override
    public int getSessionMaxAge() {
        return 60 * 60 * 24 * 7;
    }

    protected String generateRandomSessionId() {
        return UUID.randomUUID().toString();
    }
}
