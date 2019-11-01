/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.spring.boot;

import com.larksuite.appframework.sdk.LarkAppInstance;
import com.larksuite.appframework.sdk.utils.MixUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class LarkNotifyReceiver extends HttpServlet implements ApplicationContextAware {

    private static final Logger LOGGER = LoggerFactory.getLogger(LarkNotifyReceiver.class);

    private ApplicationContext applicationContext;

    public static final String CALLBACK_EVENT_PATH = "event";

    public static final String CARD_EVENT_PATH = "card";

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) {

        String path;
        {
            String requestUri = req.getRequestURI();
            String basePath = req.getServletPath();
            path = fixSlashes(requestUri.substring(basePath.length()));
        }

        String[] parts = path.split("/");

        if (parts.length != 2) {
            return;
        }

        String type = parts[0];
        String appShortName = parts[1];


        LarkAppInstance instance = getInstance(appShortName);

        if (instance == null) {
            LOGGER.warn("unknown appName: {}", appShortName);
            return;
        }

        try {
            PrintWriter writer = resp.getWriter();
            String respData = "";
            if (CALLBACK_EVENT_PATH.equals(type)) {
                respData = instance.receiveLarkNotify(readRequestData(req));
            } else if (CARD_EVENT_PATH.equals(type)) {
                respData = instance.receiveCardNotify(readRequestData(req), req);
            } else {
                LOGGER.warn("unknown event type: {}", type);
            }

            writer.print(respData);
            writer.flush();

        } catch (IOException ioe) {
            LOGGER.error("read data exception", ioe);
        }
    }


    private LarkAppInstance getInstance(String requestAppName) {
        Map<String, LarkAppInstance> beansOfType = applicationContext.getBeansOfType(LarkAppInstance.class);
        return beansOfType.values().stream().filter(ins -> ins.getAppShortName().equals(requestAppName)).findFirst().orElse(null);
    }

    private String readRequestData(HttpServletRequest req) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (InputStream is = req.getInputStream()) {
            MixUtils.copyStream(is, bos);
        }

        return new String(bos.toByteArray(), StandardCharsets.UTF_8);
    }

    private String fixSlashes(String path) {

        if (path.startsWith("/")) {
            path = path.substring(1);
        }

        if (path.length() == 0) {
            return path;
        }

        int endPos = path.length() - 1;
        for (; endPos >= 0 && path.charAt(endPos) == '/'; endPos--) {
        }

        return path.substring(0, endPos + 1);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
