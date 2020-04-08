/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.google.common.collect.Lists;
import com.larksuite.appframework.sdk.client.BatchMessageDestination;
import com.larksuite.appframework.sdk.client.ChatInfo;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.Message;
import com.larksuite.appframework.sdk.client.message.TextMessage;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import com.larksuite.appframework.sdk.core.protocol.common.User;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.Constants;
import com.larksuite.appframework.sdk.utils.MockOpenApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.List;

import static com.larksuite.appframework.sdk.utils.Constants.CHART_ID;
import static com.larksuite.appframework.sdk.utils.Constants.INVALID_OPEN_IDS;
import static com.larksuite.appframework.sdk.utils.Constants.INVALID_USER_IDS;
import static com.larksuite.appframework.sdk.utils.Constants.OPEN_ID;
import static com.larksuite.appframework.sdk.utils.Constants.OPEN_IDS;
import static com.larksuite.appframework.sdk.utils.Constants.RESPONSE_CODE;
import static com.larksuite.appframework.sdk.utils.Constants.USER_ID;
import static com.larksuite.appframework.sdk.utils.Constants.USER_IDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class LarkClientInternalTest {

    static LarkClient larkClient;


    @BeforeAll
    public static void buildClient() {

        OpenApiClient openApiClient = new MockOpenApiClient(null, null);

        App app = new App(Constants.APP_NAME, Constants.APP_ID, Constants.APP_SECRET, null, null, false);

        InstanceContext instanceContext = new InstanceContext(app, openApiClient);
        instanceContext.createTokenCenter(null);

        larkClient = new LarkClient(instanceContext, openApiClient);
    }


    @Test
    public void sendChatMessage() throws LarkClientException {
        Message message = new TextMessage("message content");
        String messageId = larkClient.sendChatMessage(MessageDestinations.chatId(Constants.CHART_ID), message);
        Assertions.assertEquals(messageId, Constants.MESSAGE_ID);
    }

    @Test
    public void batchSendChatMessage() throws LarkClientException {
        BatchMessageDestination batchMessageDestination = new BatchMessageDestination();
        batchMessageDestination.setDepartmentIds(Constants.DEPARTMENT_IDS);
        batchMessageDestination.setOpenIds(OPEN_IDS);
        batchMessageDestination.setUserIds(Constants.USER_IDS);


        Message message = new TextMessage("message content");

        LarkClient.BatchSendChatMessageResult result = larkClient.batchSendChatMessage(batchMessageDestination, message);
        assertIterableEquals(result.getInvalidDepartmentIds(), Constants.INVALID_DEPARTMENT_IDS);
        assertIterableEquals(result.getInvalidOpenIds(), Constants.INVALID_OPEN_IDS);
        assertIterableEquals(result.getInvalidUserIds(), INVALID_USER_IDS);
        Assertions.assertEquals(result.getMessageId(), Constants.MESSAGE_ID);
    }


    @Test
    public void uploadImage() throws LarkClientException {

        LarkClient.UploadImageResult result = larkClient.uploadImage("originName", new ByteArrayInputStream(Constants.IMAGE_CONTENT));

        assertEquals(result.getImageKey(), "imageKey");
    }

    @Test
    public void fetchGroupList() throws LarkClientException {
        LarkClient.GroupListResult groupListResult = larkClient.fetchGroupList(5, "0");

        assertEquals(groupListResult.getHasMore(), true);
        assertEquals(groupListResult.getPageToken(), "xx");
        assertEquals(0, groupListResult.getGroups().size());
    }

    @Test
    public void fetchGroupInfo() throws LarkClientException {

        LarkClient.GroupInfoResult groupInfoResult = larkClient.fetchGroupInfo(Constants.CHART_ID);

        assertEquals(groupInfoResult.getAvatar(), "avatar");
        Assertions.assertEquals(groupInfoResult.getChatId(), Constants.CHART_ID);
        assertEquals(groupInfoResult.getDescription(), "description");

        {
            assertEquals(groupInfoResult.getMembers().size(), 1);
            User user = groupInfoResult.getMembers().get(0);
            assertEquals(user.getOpenId(), "openId");
            assertEquals(user.getUserId(), "userId");
        }


        assertEquals(groupInfoResult.getName(), "name");
        assertEquals(groupInfoResult.getOwnerOpenId(), "ownerOpenId");
        assertEquals(groupInfoResult.getOwnerUserId(), "ownerUserId");
    }

    @Test
    public void createChat() throws LarkClientException {
        ChatInfo ci = new ChatInfo();
        ci.setName("chat name");

        ci.setOpenIds(OPEN_IDS);
        ci.setUserIds(USER_IDS);

        ci.setOnlyOwnerAdd(true);
        ci.setOnlyOwnerAtAll(true);
        ci.setOnlyOwnerEdit(false);
        ci.setShareAllowed(false);

        I18nText i18nName = new I18nText();
        i18nName.setEnUs("enUs");
        ci.setI18nNames(i18nName);

        LarkClient.UserInChatOperationResult result = larkClient.createChat(ci);

        assertIterableEquals(INVALID_OPEN_IDS, result.getInvalidOpenIds());
        assertIterableEquals(INVALID_USER_IDS, result.getInvalidUserIds());
    }

    @Test
    public void updateChatInfo() throws LarkClientException {
        ChatInfo ci = new ChatInfo();
        ci.setName("chat name");

        ci.setOwnerOpenId(OPEN_ID);
        ci.setOwnerUserId(USER_ID);

        ci.setOnlyOwnerAdd(true);
        ci.setOnlyOwnerAtAll(true);
        ci.setOnlyOwnerEdit(false);
        ci.setShareAllowed(false);

        I18nText i18nName = new I18nText();
        i18nName.setEnUs("enUs");
        ci.setI18nNames(i18nName);

        String r = larkClient.updateChatInfo(CHART_ID, ci);

        assertEquals(CHART_ID, r);
    }

    @Test
    public void addUserToChat() throws LarkClientException {

        User user1 = new User();
        user1.setOpenId(OPEN_ID);

        User user2 = new User();
        user2.setUserId(USER_ID);

        List<User> users = Lists.newArrayList(user1, user2);

        LarkClient.UserInChatOperationResult result = larkClient.addUserToChat(CHART_ID, users);

        assertIterableEquals(INVALID_OPEN_IDS, result.getInvalidOpenIds());
        assertIterableEquals(INVALID_USER_IDS, result.getInvalidUserIds());
    }


    @Test
    public void deleteUserFromChat() throws LarkClientException {
        User user = new User();
        user.setOpenId(OPEN_ID);
        user.setUserId(USER_ID);

        List<User> users = Lists.newArrayList(user);

        LarkClient.UserInChatOperationResult result = larkClient.deleteUserFromChat(CHART_ID, users);

        assertIterableEquals(INVALID_OPEN_IDS, result.getInvalidOpenIds());
        assertIterableEquals(INVALID_USER_IDS, result.getInvalidUserIds());
    }


    @Test
    public void disbandChat() {

        try {
            larkClient.disbandChat(CHART_ID);
        } catch (LarkClientException e) {
            assertEquals(RESPONSE_CODE, e.getCode());
        }
    }
}