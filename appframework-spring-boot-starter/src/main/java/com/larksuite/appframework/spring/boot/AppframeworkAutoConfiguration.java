/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.spring.boot;


import com.larksuite.appframework.sdk.AppConfiguration;
import com.larksuite.appframework.sdk.AppEventListener;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.client.ImageKeyStorage;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.utils.SimpleHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;

import javax.servlet.http.HttpServlet;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableConfigurationProperties({AppframeworkProperties.class})
@ConditionalOnClass({LarkAppInstance.class})
public class AppframeworkAutoConfiguration implements ApplicationContextAware, BeanDefinitionRegistryPostProcessor, BeanPostProcessor, EnvironmentAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppframeworkAutoConfiguration.class);

    private AppframeworkProperties appframeworkProperties;

    private ApplicationContext applicationContext;

    private Environment environment;

    private Map<String, LarkAppInstance> instanceMap = new HashMap<>();

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

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        BindResult<AppframeworkProperties> bind = Binder.get(environment).bind("larksuite.appframework", Bindable.of(AppframeworkProperties.class));
        AppframeworkProperties p = bind.get();
        if (p == null) {
            return;
        }

        appframeworkProperties = p;
        if (p.getApps() == null) {
            return;
        }

        appframeworkProperties.getApps().forEach(AppConfiguration::checkConfiguration);

        p.getApps().forEach(c -> {
            OpenApiClient openApiClient;
            if (appframeworkProperties.getDomain() != null) {
                openApiClient = new OpenApiClient(new SimpleHttpClient(), appframeworkProperties.getDomain());
            } else {
                openApiClient =  new OpenApiClient(new SimpleHttpClient(), Boolean.TRUE == appframeworkProperties.getFeishu());
            }

            LarkAppInstance ins = new LarkAppInstance(new InstanceContext(new App(c),openApiClient));

            final String appName = ins.getAppShortName();

            DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();

            String larkAppInstanceName = appName + "LarkAppInstance";
            String larkClientName = appName + "LarkClient";
            beanFactory.registerSingleton(larkAppInstanceName, ins);
            beanFactory.registerSingleton(larkClientName, ins.getLarkClient());

            if (!ins.getApp().getIsIsv()) {
                ins.getInstanceContext().createTokenCenter(null);
            }
            instanceMap.put(appName, ins);

            LOGGER.info("create LarkAppInstance bean, name: {} ", larkAppInstanceName);
            LOGGER.info("create LarkClient bean, name: {} ", larkClientName);
        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    private AppframeworkComponentInitializer appframeworkComponentInitializer = new AppframeworkComponentInitializer();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (appframeworkComponentInitializer.isComponent(bean)) {
            appframeworkComponentInitializer.addComponent(bean);
        }

        return bean;
    }

    private class AppframeworkComponentInitializer {

        AppTicketStorage appTicketStorage;
        SessionManager sessionManager;
        ImageKeyStorage imageKeyStorage;

        boolean appTicketStorageInited = false;
        boolean imageKeyStorageInited = false;
        boolean sessionManagerInited = false;

        boolean isComponent(Object bean) {
            return (bean instanceof AppTicketStorage || bean instanceof SessionManager || bean instanceof ImageKeyStorage);
        }

        void addComponent(Object bean) {

            if (bean instanceof AppTicketStorage) {
                checkBeanDuplicated(appTicketStorage, AppTicketStorage.class);
                appTicketStorage = (AppTicketStorage) bean;

            } else if (bean instanceof SessionManager) {
                checkBeanDuplicated(sessionManager, SessionManager.class);
                sessionManager = (SessionManager) bean;

            } else if (bean instanceof ImageKeyStorage) {
                checkBeanDuplicated(imageKeyStorage, ImageKeyStorage.class);
                imageKeyStorage = (ImageKeyStorage) bean;
            }

            tryInitAll();
        }

        void tryInitAll() {
            if (!appTicketStorageInited) {
                initAppTicketStorage();
            }
            if (appTicketStorageInited && !imageKeyStorageInited) {
                initImageKeyStorage();
            }
            if (appTicketStorageInited && !sessionManagerInited) {
                initSessionManager();
            }
        }

        void initAppTicketStorage() {
            if (appTicketStorage != null) {
                for (LarkAppInstance ins : instanceMap.values()) {
                    if (ins.getApp().getIsIsv()) {
                        ins.getInstanceContext().createTokenCenter(appTicketStorage);
                        LOGGER.info("find appTicketStorage for ISV app: {}", ins.getAppShortName());
                    }
                }
                appTicketStorageInited = true;
            }
        }

        void initSessionManager() {
            if (sessionManager != null) {
                for (LarkAppInstance ins : instanceMap.values()) {
                    ins.getInstanceContext().createMiniProgramAuthenticator(sessionManager, appframeworkProperties.getCookieDomainParentLevel());

                    LOGGER.info("create MiniProgramAuthenticator for app: {}", ins.getAppShortName());
                }
                sessionManagerInited = true;
            }
        }

        void initImageKeyStorage() {
            if (imageKeyStorage != null) {
                for (LarkAppInstance ins : instanceMap.values()) {
                    ins.getInstanceContext().createImageKeyManager(imageKeyStorage);

                    LOGGER.info("create ImageKeyManager for app: {}", ins.getAppShortName());
                }
                imageKeyStorageInited = true;
            }
        }

        void checkBeanDuplicated(Object existed, Class<?> clazz) {
            if (existed == null) {
                return;
            }
            throw new IllegalStateException("find multi beans " + clazz.getName());
        }
    }

    @EventListener(ApplicationStartedEvent.class)
    public void onApplicationEvent(ApplicationStartedEvent event) {

        if (!appframeworkComponentInitializer.appTicketStorageInited) {
            for (LarkAppInstance ins : instanceMap.values()) {
                if (ins.getApp().getIsIsv()) {
                    throw new IllegalStateException("cannot find an unique AppTicketStorage for ISV app " + ins.getAppShortName());
                }
            }
        }

        EventListenerScanner sc = new EventListenerScanner(appframeworkProperties, applicationContext);
        Map<String, AppEventListener> map = sc.scanEventListeners();

        for (LarkAppInstance ins : instanceMap.values()) {
            AppEventListener appEventListener = map.get(ins.getAppShortName());
            if (appEventListener != null) {
                ins.setAppEventListener(appEventListener);
                LOGGER.info("app {} listening events: {}", ins.getAppShortName(), appEventListener.toString());
            } else {
                LOGGER.warn("app {} not listening any event", ins.getAppShortName());
            }
        }

        for (LarkAppInstance ins : instanceMap.values()) {
            ins.init();
        }

        LOGGER.info("apps initializing done, apps: {}", instanceMap.values().stream().map(LarkAppInstance::getAppShortName).collect(Collectors.joining(",")));
    }
}
