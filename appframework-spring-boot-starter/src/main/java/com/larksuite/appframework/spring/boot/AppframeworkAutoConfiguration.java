/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.spring.boot;


import com.larksuite.appframework.sdk.AppConfiguration;
import com.larksuite.appframework.sdk.LarkAppInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpServlet;

@Configuration
@EnableConfigurationProperties({AppframeworkProperties.class})
@ConditionalOnClass({LarkAppInstance.class})
public class AppframeworkAutoConfiguration implements ApplicationContextAware, InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppframeworkAutoConfiguration.class);

    @Autowired
    private AppframeworkProperties appframeworkProperties;

    protected ApplicationContext applicationContext;


    @Override
    public void afterPropertiesSet() throws Exception {

        new EventHandlerScanner(appframeworkProperties, applicationContext).scan();
    }


    @ConditionalOnProperty("larksuite.appframework.notify.base-path")
    @Bean
    public ServletRegistrationBean<HttpServlet> restApi() {

        printReceiverInfo();

        LarkNotifyReceiver larkNotifyReceiver = new LarkNotifyReceiver();
        larkNotifyReceiver.setApplicationContext(applicationContext);

        ServletRegistrationBean<HttpServlet> servletRegistrationBean = new ServletRegistrationBean<>(
                larkNotifyReceiver,
                checkAndFixBasePath(appframeworkProperties.getNotify().getBasePath()) + "/*"
        );
        servletRegistrationBean.setName("LarkNotifyReceiver");
        return servletRegistrationBean;
    }

    private static String checkAndFixBasePath(String p) {

        String basePath = p;

        if (basePath.equals("/")) {
            throw new IllegalStateException("Lark notify base path could not be a root path: " + p);
        }

        // remove all ending slash
        while (basePath.endsWith("/")) {
            basePath = basePath.substring(0, basePath.length() - 1);
        }

        if (basePath.length() == 0) {
            throw new IllegalStateException("Lark notify base path illegal, base path: " + p);
        }

        if (!basePath.startsWith("/")) {
            basePath = "/" + basePath;
        }

        return basePath;
    }


    private void printReceiverInfo() {
        LOGGER.info("");
        LOGGER.info("");
        LOGGER.info(" ------------------------------------------------------------");
        LOGGER.info("|                   Lark Appframework                       |");
        LOGGER.info(" ------------------------------------------------------------");

        for (AppConfiguration app : appframeworkProperties.getApps()) {
            LOGGER.info("|");
            LOGGER.info("| APP SHORT NAME[ {} ]", app.getAppShortName());
            LOGGER.info("| callback event uri: {}", checkAndFixBasePath(appframeworkProperties.getNotify().getBasePath()) + "/event/" + app.getAppShortName());
            LOGGER.info("|     card event uri: {}", checkAndFixBasePath(appframeworkProperties.getNotify().getBasePath()) + "/card/" + app.getAppShortName());
            LOGGER.info("|");
            LOGGER.info(" ------------------------------------------------------------");
        }
        LOGGER.info("");
        LOGGER.info("");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
