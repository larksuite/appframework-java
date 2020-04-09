/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.utils;

import com.google.common.collect.Lists;
import com.larksuite.appframework.sdk.core.protocol.*;
import com.larksuite.appframework.sdk.core.protocol.common.User;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import org.junit.jupiter.api.Assertions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.larksuite.appframework.sdk.utils.Constants.*;
import static com.larksuite.appframework.sdk.utils.Constants.APP_SECRET;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

public class MockOpenApiClient extends OpenApiClient {

    public MockOpenApiClient(HttpClient httpClient, String basePath) {
        super(httpClient, basePath);
    }

    @Override
    public SendMessageResponse sendMessage(String tenantAccessToken, SendMessageRequest req) throws LarkClientException {

        Assertions.assertEquals(req.getChatId(), CHART_ID);
        Assertions.assertEquals(req.getOpenId(), null);
        Assertions.assertEquals(req.getMsgType(), "text");

        Assertions.assertTrue(TestUtils.objectEqualsJson(req.getContent(), "{\"text\":\"message content\"}"));

        SendMessageResponse response = new SendMessageResponse();
        response.setCode(0);
        response.setMsg("");

        SendMessageResponse.Data data = new SendMessageResponse.Data();
        data.setMessageId(MESSAGE_ID);
        response.setData(data);
        return response;
    }

    @Override
    public SendMessageBatchResponse sendMessageBatch(String tenantAccessToken, SendMessageBatchRequest req) throws LarkClientException {

        Assertions.assertIterableEquals(req.getDepartmentIds(), DEPARTMENT_IDS);
        Assertions.assertEquals(req.getMsgType(), "text");
        Assertions.assertIterableEquals(req.getOpenIds(), OPEN_IDS);
        Assertions.assertIterableEquals(req.getUserIds(), USER_IDS);
        Assertions.assertTrue(TestUtils.objectEqualsJson(req.getContent(), "{\"text\":\"message content\"}"));

        SendMessageBatchResponse response = new SendMessageBatchResponse();
        response.setCode(0);
        response.setMsg("");

        SendMessageBatchResponse.Data data = new SendMessageBatchResponse.Data();

        data.setMessageId(MESSAGE_ID);
        data.setInvalidDepartmentIds(INVALID_DEPARTMENT_IDS);
        data.setInvalidOpenIds(INVALID_OPEN_IDS);
        data.setInvalidUserIds(INVALID_USER_IDS);

        response.setData(data);
        return response;
    }

    @Override
    public UploadImageResponse uploadImage(String tenantAccessToken, UploadImageRequest req) throws LarkClientException {


        assertEquals(req.getOriginName(), "originName");

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            MixUtils.copyStream(req.getImageFile(), bos);

            assertArrayEquals(bos.toByteArray(), IMAGE_CONTENT);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        UploadImageResponse response = new UploadImageResponse();
        response.setCode(0);
        response.setMsg("");

        UploadImageResponse.Data data = new UploadImageResponse.Data();
        data.setImageKey("imageKey");
        data.setUrl("url");
        response.setData(data);

        return response;
    }

    @Override
    public FetchChatInfoResponse fetchChatInfo(String tenantAccessToken, FetchChatInfoRequest req) throws LarkClientException {

        Assertions.assertEquals(req.getChatId(), CHART_ID);

        FetchChatInfoResponse response = new FetchChatInfoResponse();

        response.setCode(0);
        response.setMsg("");

        FetchChatInfoResponse.Data data = new FetchChatInfoResponse.Data();
        data.setChatId(CHART_ID);
        data.setAvatar("avatar");
        data.setDescription("description");
        data.setName("name");
        data.setOwnerOpenId("ownerOpenId");
        data.setOwnerUserId("ownerUserId");

        User user = new User();
        user.setOpenId("openId");
        user.setUserId("userId");
        data.setMembers(Lists.newArrayList(user));
        response.setData(data);

        return response;
    }

    @Override
    public FetchChatListResponse fetchChatList(String tenantAccessToken, FetchChatListRequest req) throws LarkClientException {

        Assertions.assertEquals(req.getPageSize(), 5);
        Assertions.assertEquals(req.getPageToken(), "0");

        FetchChatListResponse response = new FetchChatListResponse();

        response.setCode(0);
        response.setMsg("");

        FetchChatListResponse.Data data = new FetchChatListResponse.Data();
        data.setHasMore(true);
        data.setPageToken("xx");
        data.setGroups(new ArrayList<>());
        response.setData(data);

        return response;
    }

    @Override
    public FetchAppAccessTokenInternalResponse fetchAppAccessTokenInternal(FetchAppAccessTokenInternalRequest req) throws LarkClientException {
        Assertions.assertEquals(req.getAppId(), APP_ID);
        Assertions.assertEquals(req.getAppSecret(), APP_SECRET);

        FetchAppAccessTokenInternalResponse response = new FetchAppAccessTokenInternalResponse();
        response.setCode(0);
        response.setMsg("");
        response.setAppAccessToken(APP_ACCESS_TOKEN);
        response.setExpire("6000");

        return response;
    }

    @Override
    public FetchTenantAccessTokenInternalResponse fetchTenantAccessTokenInternal(FetchTenantAccessTokenInternalRequest req) throws LarkClientException {
        Assertions.assertEquals(req.getAppId(), APP_ID);
        Assertions.assertEquals(req.getAppSecret(), APP_SECRET);

        FetchTenantAccessTokenInternalResponse response = new FetchTenantAccessTokenInternalResponse();
        response.setCode(0);
        response.setMsg("");
        response.setTenantAccessToken(TENANT_ACCESS_TOKEN);
        response.setExpire("6000");

        return response;
    }


    @Override
    public FetchAppAccessTokenIsvResponse fetchAppAccessTokenIsv(FetchAppAccessTokenIsvRequest req) throws LarkClientException {
        Assertions.assertEquals(req.getAppId(), APP_ID);
        Assertions.assertEquals(req.getAppSecret(), APP_SECRET);
        Assertions.assertEquals(req.getAppTicket(), APP_TICKET);

        FetchAppAccessTokenIsvResponse response = new FetchAppAccessTokenIsvResponse();
        response.setCode(0);
        response.setMsg("");
        response.setAppAccessToken(APP_ACCESS_TOKEN);
        response.setExpire("6000");

        return response;
    }

    @Override
    public FetchTenantAccessTokenIsvResponse fetchTenantAccessTokenIsv(FetchTenantAccessTokenIsvRequest req) throws LarkClientException {

        Assertions.assertEquals(req.getAppAccessToken(), APP_ACCESS_TOKEN);
        Assertions.assertEquals(req.getTenantKey(), TENANT_KEY);

        FetchTenantAccessTokenIsvResponse response = new FetchTenantAccessTokenIsvResponse();
        response.setCode(0);
        response.setMsg("");
        response.setExpire("6000");
        response.setTenantAccessToken(TENANT_ACCESS_TOKEN);

        return response;
    }

    @Override
    public ResendAppTicketResponse resendAppTicket(ResendAppTicketRequest req) throws LarkClientException {
        Assertions.assertEquals(req.getAppId(), APP_ID);
        Assertions.assertEquals(req.getAppSecret(), APP_SECRET);

        return new ResendAppTicketResponse();
    }

    @Override
    public MiniProgramLoginResponse miniProgramLoginValidate(String appAccessToken, MiniProgramLoginRequest req) throws LarkClientException {
        Assertions.assertEquals(APP_ACCESS_TOKEN, appAccessToken);
        Assertions.assertEquals(CODE, req.getCode());

        MiniProgramLoginResponse response = new MiniProgramLoginResponse();
        response.setCode(0);
        response.setMsg("");
        MiniProgramLoginResponse.Data data = new MiniProgramLoginResponse.Data();


        data.setEmployeeId("employeeId");
        data.setOpenId("openId");

        response.setData(data);
        return response;
    }

    @Override
    public UpdateChatInfoResponse updateChatInfo(String tenantAccessToken, UpdateChatInfoRequest req) throws LarkClientException {
        assertEquals("chat name", req.getName());
        assertEquals(OPEN_ID, req.getOwnerOpenId());
        assertEquals(USER_ID, req.getOwnerUserId());
        assertEquals(true, req.getOnlyOwnerAdd());
        assertEquals(true, req.getOnlyOwnerAtAll());
        assertEquals(false, req.getOnlyOwnerEdit());
        assertEquals(false, req.getShareAllowed());

        UpdateChatInfoResponse response = new UpdateChatInfoResponse();
        response.setCode(0);
        response.setMsg("");

        UpdateChatInfoResponse.Data data = new UpdateChatInfoResponse.Data();
        data.setChatId(CHART_ID);
        response.setData(data);
        return response;
    }

    @Override
    public CreateChatResponse createChat(String tenantAccessToken, CreateChatRequest req) throws LarkClientException {

        assertEquals("chat name", req.getName());
        assertIterableEquals(OPEN_IDS, req.getOpenIds());
        assertIterableEquals(USER_IDS, req.getUserIds());
        assertEquals(true, req.getOnlyOwnerAdd());
        assertEquals(true, req.getOnlyOwnerAtAll());
        assertEquals(false, req.getOnlyOwnerEdit());
        assertEquals(false, req.getShareAllowed());


        assertEquals("enUs", req.getI18nNames().getEnUs());



        CreateChatResponse response = new CreateChatResponse();
        response.setCode(0);
        response.setMsg("");

        CreateChatResponse.Data data = new CreateChatResponse.Data();
        data.setInvalidOpenIds(INVALID_OPEN_IDS);
        data.setInvalidUserIds(INVALID_USER_IDS);

        response.setData(data);
        return response;
    }

    @Override
    public AddUserToChatResponse addUserToChat(String tenantAccessToken, AddUserToChatRequest req) throws LarkClientException {

        assertEquals(CHART_ID, req.getChatId());
        assertIterableEquals(OPEN_IDS, req.getOpenIds());
        assertIterableEquals(USER_IDS, req.getUserIds());

        AddUserToChatResponse response = new AddUserToChatResponse();
        response.setCode(0);
        response.setMsg("");

        AddUserToChatResponse.Data data = new AddUserToChatResponse.Data();
        data.setInvalidOpenIds(INVALID_OPEN_IDS);
        data.setInvalidUserIds(INVALID_USER_IDS);

        response.setData(data);
        return response;

    }

    @Override
    public DeleteUserFromChatResponse deleteUserFromChat(String tenantAccessToken, DeleteUserFromChatRequest req) throws LarkClientException {
        DeleteUserFromChatResponse response = new DeleteUserFromChatResponse();
        response.setCode(0);
        response.setMsg("");

        DeleteUserFromChatResponse.Data data = new DeleteUserFromChatResponse.Data();
        data.setInvalidOpenIds(INVALID_OPEN_IDS);
        data.setInvalidUserIds(INVALID_USER_IDS);

        response.setData(data);
        return response;
    }

    @Override
    public DisbandChatResponse disbandChat(String tenantAccessToken, DisbandChatRequest req) throws LarkClientException {
        DisbandChatResponse response = new DisbandChatResponse();
        response.setCode(RESPONSE_CODE);
        response.setMsg("some exception");

        return response;
    }
}
