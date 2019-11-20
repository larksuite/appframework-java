/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk;

import com.larksuite.appframework.sdk.client.BatchMessageDestination;
import com.larksuite.appframework.sdk.client.LarkClient;
import com.larksuite.appframework.sdk.client.MessageDestinations;
import com.larksuite.appframework.sdk.client.message.Message;
import com.larksuite.appframework.sdk.client.message.TextMessage;
import com.larksuite.appframework.sdk.core.App;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.core.auth.TokenCenter;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.core.protocol.common.User;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.Constants;
import com.larksuite.appframework.sdk.utils.MockOpenApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;

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
        batchMessageDestination.setOpenIds(Constants.OPEN_IDS);
        batchMessageDestination.setUserIds(Constants.USER_IDS);


        Message message = new TextMessage("message content");

        LarkClient.BatchSendChatMessageResult result = larkClient.batchSendChatMessage(batchMessageDestination, message);
        assertIterableEquals(result.getInvalidDepartmentIds(), Constants.INVALID_DEPARTMENT_IDS);
        assertIterableEquals(result.getInvalidOpenIds(), Constants.INVALID_OPEN_IDS);
        assertIterableEquals(result.getInvalidUserIds(), Constants.INVALID_USER_IDS);
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
}
