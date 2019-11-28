/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.larksuite.appframework.sdk.utils.MixUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Properties;

@Getter
@Setter
@ToString
public class AppConfiguration {

    /**
     * app short name defined by app developer
     *
     * @parm appShortName
     */
    private String appShortName;

    private String appId;

    private String appSecret;

    private String encryptKey;

    private String verificationToken;

    private Boolean isIsv = Boolean.FALSE;

    public static void checkConfiguration(AppConfiguration c) {
        if (c == null) {
            throw new IllegalArgumentException("ClientConfiguration should not be null");
        }

        if (MixUtils.isBlankString(c.appId)
                || MixUtils.isBlankString(c.appShortName)
                || MixUtils.isBlankString(c.getAppSecret())) {
            throw new IllegalArgumentException("illegal app info: " + c);
        }
    }

    /**
     *
     * larksuite.appframework.${appName1}.appId=xxx
     * larksuite.appframework.${appName1}.appSecret=
     * larksuite.appframework.${appName1}.encryptKey=
     * larksuite.appframework.${appName1}.verificationToken=
     * larksuite.appframework.${appName1}.isIsv=true
     * @param appShortName
     * @param properties
     * @return AppConfiguration
     */
    public static AppConfiguration loadFromProperties(String appShortName, Properties properties) {

        final String pre = "larksuite.appframework." + appShortName;
        String appId = properties.getProperty(pre + ".appId");
        String appSecret = properties.getProperty(pre + ".appSecret");
        String encryptKey = properties.getProperty(pre + ".encryptKey");
        String verificationToken = properties.getProperty(pre + ".verificationToken");
        String isIsv = properties.getProperty(pre + ".isIsv");

        AppConfiguration ac = new AppConfiguration();
        ac.setAppShortName(appShortName);
        ac.setAppId(appId);
        ac.setAppSecret(appSecret);
        ac.setEncryptKey(encryptKey);
        ac.setVerificationToken(verificationToken);
        ac.setIsIsv(Boolean.parseBoolean(isIsv));

        return ac;
    }
}


