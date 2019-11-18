/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.spring.boot;

import com.larksuite.appframework.sdk.AppConfiguration;
import com.larksuite.appframework.sdk.AppEventListener;
import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.LarkAppInstanceFactory;
import com.larksuite.appframework.sdk.client.ImageKeyStorage;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.SessionManager;
import com.larksuite.appframework.sdk.client.message.card.Card;
import com.larksuite.appframework.sdk.client.message.card.CardActionUtils;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.AppTicketStorage;
import com.larksuite.appframework.sdk.core.eventhandler.CardEventHandler;
import com.larksuite.appframework.sdk.core.eventhandler.EventCallbackHandler;
import com.larksuite.appframework.sdk.core.protocol.card.CardEvent;
import com.larksuite.appframework.sdk.core.protocol.event.BaseEvent;
import com.larksuite.appframework.sdk.utils.HttpClient;
import com.larksuite.appframework.sdk.utils.MixUtils;
import com.larksuite.appframework.spring.boot.annotation.CardAction;
import com.larksuite.appframework.spring.boot.annotation.Handler;
import com.larksuite.appframework.spring.boot.annotation.LarkEventHandlers;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EventHandlerScanner {

    private AppframeworkProperties appframeworkProperties;
    private ApplicationContext applicationContext;

    public EventHandlerScanner(AppframeworkProperties appframeworkProperties, ApplicationContext applicationContext) {
        this.appframeworkProperties = appframeworkProperties;
        this.applicationContext = applicationContext;
    }

    public void scan() {
        if (appframeworkProperties.getApps().isEmpty()) {
            return;
        }

        appframeworkProperties.getApps().forEach(AppConfiguration::checkConfiguration);

        final Map<String, AppHandlers> appEventHandlers = scanEventHandlers();

        appframeworkProperties.getApps().forEach(ac -> {

            AppHandlers handlers = appEventHandlers.get(ac.getAppShortName());

            LarkAppInstance larkAppInstance = createLarkAppInstance(ac, handlers);

            registerLarkAppInstance(larkAppInstance);
        });
    }

    private Map<String, AppHandlers> scanEventHandlers() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(LarkEventHandlers.class);

        // appName -> eventType -> invocation
        final Map<String, AppHandlers> appEventHandlers = new HashMap<>();

        for (Object bean : beans.values()) {

            List<Invocation> invocations = findInvocationsFromBean(bean);

            invocations.forEach(ii -> {

                final String appName = getAppName(ii);

                AppHandlers appHandlers = appEventHandlers.computeIfAbsent(appName, kk -> new AppHandlers());

                if (ii instanceof EventInvocation) {
                    final EventInvocation inv = (EventInvocation) ii;

                    // callback event
                    if (BaseEvent.class.isAssignableFrom(inv.eventType)) {

                        if (null != appHandlers.callbackEventInvocations.putIfAbsent((Class<? extends BaseEvent>) inv.eventType, inv)) {
                            throw new IllegalStateException("duplicate handler for " + inv.eventType.getSimpleName() + ", appName: " + appName);
                        }

                    } else if (CardEvent.class == inv.eventType) {

                        if (appHandlers.cardEventInvocation != null) {
                            throw new IllegalStateException("duplicate handler for " + inv.eventType.getSimpleName() + ", appName: " + appName);
                        }

                        appHandlers.cardEventInvocation = inv;

                    } else {
                        throw new IllegalStateException("[BUG]unreachable code, unknown eventType: " + inv.eventType.getName());
                    }
                } else if (ii instanceof CardActionInvocation) {

                    final CardActionInvocation inv = (CardActionInvocation) ii;

                    if (null != appHandlers.cardActionInvocations.putIfAbsent(inv.cardActionAnnotation.methodName(), inv)) {
                        throw new IllegalStateException("duplicate handler for card method " + inv.cardActionAnnotation.methodName() + ", appName: " + appName);
                    }
                }
            });
        }

        return appEventHandlers;
    }

    private String getAppName(Invocation invocation) {
        // single app project, ignore app names in annotations
        if (appframeworkProperties.getApps().size() == 1) {
            return appframeworkProperties.getApps().get(0).getAppShortName();
        }

        final String name = invocation.getAppName();
        if (appframeworkProperties.getApps().stream().noneMatch(a -> a.getAppShortName().equals(name))) {
            throw new IllegalStateException("unknown app name " + invocation.getAppName());
        }

        return name;
    }

    private void registerLarkAppInstance(LarkAppInstance ins) {
        final String appName = ins.getAppShortName();

        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) ((ConfigurableApplicationContext) applicationContext).getBeanFactory();
        beanFactory.registerSingleton(appName + "LarkAppInstance", ins);
        beanFactory.registerSingleton(appName + "LarkClient", ins.getLarkClient());

        if (ins.getMiniProgramAuthenticator() != null) {
            beanFactory.registerSingleton(appName + "MiniProgramAuthenticator", ins.getMiniProgramAuthenticator());
        }
    }


    private LarkAppInstance createLarkAppInstance(AppConfiguration c, AppHandlers appHandlers) {

        AppEventListener listener = LarkAppInstanceFactory.createAppEventListener();

        if (appHandlers != null) {
            listener.onEvents(appHandlers.callbackEventInvocations.entrySet().stream().collect(
                    Collectors.toMap(Map.Entry::getKey, e -> methodToCallbackEventHandler(e.getKey(), e.getValue()))
            ));

            listener.onCardEvent(methodToCardEventHandler(appHandlers));
        }

        LarkAppInstanceFactory.LarkAppInstanceBuilder builder = LarkAppInstanceFactory
                .builder(c)
                .registerAppEventCallbackListener(listener);

        if (appframeworkProperties.getFeishu()) {
            builder.feishu();
        }

        if (c.getIsIsv()) {
            try {
                AppTicketStorage appTicketStorage = applicationContext.getBean(AppTicketStorage.class);
                builder.appTicketStorage(appTicketStorage);
            } catch (BeansException e) {
                throw new IllegalStateException("cannot find an unique AppTicketStorage for ISV app " + c.getAppShortName());
            }
        }

        try {
            // try to find a httpClient object from context
            builder.httpClient(applicationContext.getBean(HttpClient.class));
        } catch (NoSuchBeanDefinitionException e) {
            // ignore
        }

        try {
            builder.imageKeyStorage(applicationContext.getBean(ImageKeyStorage.class));
        } catch (NoSuchBeanDefinitionException e) {
            // ignore
        }

        try {
            SessionManager sm = applicationContext.getBean(SessionManager.class);
            builder.authenticator(sm, appframeworkProperties.getCookieDomainParentLevel());
        } catch (NoSuchBeanDefinitionException e) {

        }

        return builder.create();
    }


    private <T extends BaseEvent> EventCallbackHandler<T> methodToCallbackEventHandler(Class<T> eventType, Invocation invocation) {

        return (ic, e) -> {
            Parameter[] parameters = invocation.method.getParameters();

            Object[] params = new Object[parameters.length];

            for (int i = 0; i < params.length; i++) {
                if (parameters[i].getType() == InstanceContext.class) {
                    params[i] = ic;
                } else if (parameters[i].getType() == LarkClient.class) {
                    params[i] = ic.getLarkClient();
                } else if (parameters[i].getType() == eventType) {
                    params[i] = e;
                }
            }

            return doInvoke(invocation, params);
        };
    }

    private CardEventHandler methodToCardEventHandler(AppHandlers handlers) {
        if (handlers.cardEventInvocation == null && handlers.cardActionInvocations.size() == 0) {
            return null;
        }

        return (ic, e) -> {

            Invocation inv = null;
            {
                String methodName = null;
                if (e.getAction().getValue() != null) {
                    methodName = CardActionUtils.getActionMethodName(e);
                }

                if (methodName != null) {
                    inv = handlers.cardActionInvocations.get(methodName);
                }
            }

            if (inv == null) {
                inv = handlers.cardEventInvocation;
            }

            if (inv == null) {
                return null;
            }

            Parameter[] parameters = inv.method.getParameters();

            Object[] params = new Object[parameters.length];

            for (int i = 0; i < params.length; i++) {
                if (parameters[i].getType() == InstanceContext.class) {
                    params[i] = ic;
                } else if (parameters[i].getType() == LarkClient.class) {
                    params[i] = ic.getLarkClient();
                } else if (parameters[i].getType() == CardEvent.class) {
                    params[i] = e;
                }
            }

            return (Card) doInvoke(inv, params);
        };
    }

    private static Object doInvoke(Invocation inv, Object[] params) {
        try {
            return inv.method.invoke(inv.o, params);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException(ex);

        } catch (InvocationTargetException ex) {
            throw new RuntimeException(ex.getTargetException());
        }
    }

    private List<Invocation> findInvocationsFromBean(Object o) {
        final List<Invocation> result = new ArrayList<>();

        for (Method m : o.getClass().getMethods()) {
            Invocation inv = methodToInvocation(m, o);

            if (inv != null) {
                result.add(inv);
            }
        }

        return result;
    }

    private Invocation methodToInvocation(Method m, Object o) {
        if (m.isAnnotationPresent(Handler.class)) {
            Parameter[] parameters = m.getParameters();
            if (parameters == null) {
                return null;
            }

            Parameter eventParam = null;

            for (Parameter p : parameters) {
                if ((BaseEvent.class.isAssignableFrom(p.getType()) && BaseEvent.class != p.getType()) || (CardEvent.class == p.getType())) {

                    if (eventParam != null) {
                        throw new IllegalStateException("find multi event parameters for method: " + o.getClass().getName() + "." + m.getName());
                    }

                    eventParam = p;
                }
            }

            if (eventParam == null) {
                return null;
            }

            // check method result for card event
            if (CardEvent.class == eventParam.getType()) {
                if (m.getReturnType() != Card.class) {
                    throw new IllegalStateException("card event handler method return type must be Card, method: " + m.getName());
                }
            }

            return new EventInvocation(m, o, eventParam.getType());

        } else if (m.isAnnotationPresent(CardAction.class)) {
            if (m.getReturnType() != Card.class) {
                throw new IllegalStateException("card action handler method return type must be Card, method: " + m.getName());
            }

            return new CardActionInvocation(m, o);
        }

        return null;
    }

    private abstract static class Invocation {
        Method method;
        Object o;
        LarkEventHandlers handlersAnnotation;

        public Invocation(Method method, Object o) {
            this.method = method;
            this.o = o;
            this.handlersAnnotation = o.getClass().getAnnotation(LarkEventHandlers.class);
        }

        public String getAppName() {
            return handlersAnnotation.appName();
        }
    }

    private static class EventInvocation extends Invocation {
        Handler handlerAnnotation;
        Class<?> eventType;

        public EventInvocation(Method method, Object o, Class<?> eventType) {
            super(method, o);
            this.eventType = eventType;
            this.handlerAnnotation = method.getAnnotation(Handler.class);
        }

        @Override
        public String getAppName() {
            return MixUtils.isBlankString(super.getAppName()) ? handlerAnnotation.appName() : super.getAppName();
        }
    }

    public static class CardActionInvocation extends Invocation {

        CardAction cardActionAnnotation;

        public CardActionInvocation(Method method, Object o) {
            super(method, o);
            this.cardActionAnnotation = method.getAnnotation(CardAction.class);
        }

        @Override
        public String getAppName() {
            return MixUtils.isBlankString(super.getAppName()) ? cardActionAnnotation.appName() : super.getAppName();
        }
    }

    private static class AppHandlers {
        EventInvocation cardEventInvocation;
        Map<String, CardActionInvocation> cardActionInvocations = new HashMap<>();
        Map<Class<? extends BaseEvent>, EventInvocation> callbackEventInvocations = new HashMap<>();
    }
}
