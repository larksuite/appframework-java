/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.spring.boot;

import com.larksuite.appframework.sdk.AppConfiguration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "larksuite.appframework")
public class AppframeworkProperties {

    private List<AppConfiguration> apps;

    private Notify notify;

    private Boolean feishu;

    private String domain;

    private Integer cookieDomainParentLevel;

    @Setter
    @Getter
    public static class Notify {
        private String basePath;
    }

}
