/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core;

import com.larksuite.appframework.sdk.AppConfiguration;
import com.larksuite.appframework.sdk.core.auth.NotifyDataDecrypter;
import com.larksuite.appframework.sdk.utils.MixUtils;
import lombok.Getter;
import lombok.ToString;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Getter
@ToString
public class App {

    private final String appId;

    private final String appSecret;

    private final String encryptKey;

    private final String verificationToken;

    private final Boolean isIsv;

    private final String appShortName;

    private final NotifyDataDecrypter notifyDataDecrypter;

    public App(AppConfiguration ac) {
        this(ac.getAppShortName(),
                ac.getAppId(),
                ac.getAppSecret(),
                ac.getEncryptKey(),
                ac.getVerificationToken(),
                ac.getIsIsv()
        );
    }

    public App(String appShortName, String appId, String appSecret, String encryptKey, String verificationToken, Boolean isIsv) {
        this.appShortName = appShortName;
        this.appId = appId;
        this.appSecret = appSecret;
        this.encryptKey = encryptKey;
        this.verificationToken = verificationToken;
        this.isIsv = isIsv != null ? isIsv : Boolean.FALSE;

        this.notifyDataDecrypter = MixUtils.isBlankString(this.encryptKey) ? null : new NotifyDataDecrypter(encryptKey);
    }

    public boolean checkVerificationToken(String token) {
        if (token == null) {
            return verificationToken == null;
        }
        return token.equals(verificationToken);
    }

    public boolean needDecrypt() {
        return this.notifyDataDecrypter != null;
    }

    public String decrypt(String encryptedData) throws BadPaddingException, InvalidKeyException, NoSuchAlgorithmException, IllegalBlockSizeException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        if (needDecrypt()) {
            return notifyDataDecrypter.decrypt(encryptedData);
        } else {
            return encryptedData;
        }
    }
}
