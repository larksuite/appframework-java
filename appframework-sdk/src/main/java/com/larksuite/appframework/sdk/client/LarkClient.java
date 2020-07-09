/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.client;

import com.larksuite.appframework.sdk.core.protocol.*;
import com.larksuite.appframework.sdk.core.protocol.common.Group;
import com.larksuite.appframework.sdk.core.protocol.common.I18nText;
import com.larksuite.appframework.sdk.core.protocol.common.User;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.client.message.CardMessage;
import com.larksuite.appframework.sdk.client.message.Message;
import com.larksuite.appframework.sdk.core.InstanceContext;
import com.larksuite.appframework.sdk.utils.MixUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * LarkClient
 * botframework users should use this class to communicate with lark server
 * <p>
 * usually we don't create instance manually, just get the instance like this
 * </p>
 * <code>
 * BotAppsServer botAppsServer; // create server by factory
 * LarkClient larkClient = botAppsServer.getLarkClient();
 * </code>
 * or
 * <code>
 * ServerContext serverContext; // provided as handler parameter
 * serverContext.getLarkClient();
 * </code>
 */
public class LarkClient {

    private InstanceContext instanceContext;

    private OpenApiClient openApiClient;

    private String appId;

    public LarkClient(InstanceContext instanceContext, OpenApiClient openApiClient) {
        this.instanceContext = instanceContext;
        this.openApiClient = openApiClient;
        this.appId = this.instanceContext.getApp().getAppId();
    }


    /**
     * send chat message as ISV
     *
     * @param dest      where will this message send to
     * @param tenantKey tenantKey
     * @param message   message
     * @return messageId
     * @throws LarkClientException throws a LarkClientException
     */
    public String sendChatMessageIsv(MessageDestination dest, Message message, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doSendChatMessage(dest, message, tenantKey);
    }

    public String sendChatMessage(MessageDestination dest, Message message) throws LarkClientException {
        checkAppType(false);
        return doSendChatMessage(dest, message, null);
    }

    public BatchSendChatMessageResult batchSendChatMessageIsv(BatchMessageDestination dest, Message message, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doBatchSendChatMessage(dest, message, tenantKey);
    }

    public BatchSendChatMessageResult batchSendChatMessage(BatchMessageDestination dest, Message message) throws LarkClientException {
        checkAppType(false);
        return doBatchSendChatMessage(dest, message, null);
    }

    public UploadImageResult uploadImageIsv(String originName, InputStream imageFile, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doUploadImage(originName, imageFile, tenantKey);
    }

    public UploadImageResult uploadImage(String originName, InputStream imageFile) throws LarkClientException {
        checkAppType(false);
        return doUploadImage(originName, imageFile, null);
    }

    public InputStream fetchImageIsv(String imageKey, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doFetchImage(imageKey, tenantKey);
    }

    public InputStream fetchImage(String imageKey) throws LarkClientException {
        checkAppType(false);
        return doFetchImage(imageKey, null);
    }

    public GroupListResult fetchGroupListIsv(Integer pageSize, String pageToken, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doFetchGroupList(pageSize, pageToken, tenantKey);
    }

    public GroupListResult fetchGroupList(Integer pageSize, String pageToken) throws LarkClientException {
        checkAppType(false);
        return doFetchGroupList(pageSize, pageToken, null);
    }

    public GroupInfoResult fetchGroupInfoIsv(String chatId, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doFetchGroupInfo(chatId, tenantKey);
    }

    public GroupInfoResult fetchGroupInfo(String chatId) throws LarkClientException {
        checkAppType(false);
        return doFetchGroupInfo(chatId, null);
    }

    public UserInChatOperationResult createChat(ChatInfo chatInfo) throws LarkClientException {
        checkAppType(false);
        return doCreateChat(chatInfo, null);
    }

    public UserInChatOperationResult createChatIsv(ChatInfo chatInfo, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doCreateChat(chatInfo, tenantKey);
    }

    public String updateChatInfo(String chatId, ChatInfo chatInfo) throws LarkClientException {
        checkAppType(false);
        return doUpdateChatInfo(chatId, chatInfo, null);
    }

    public String updateChatInfoIsv(String chatId, ChatInfo chatInfo, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doUpdateChatInfo(chatId, chatInfo, tenantKey);
    }

    public UserInChatOperationResult addUserToChat(String chatId, List<User> users) throws LarkClientException {
        checkAppType(false);
        return doAddUserToChat(chatId, users, null);
    }

    public UserInChatOperationResult addUserToChatIsv(String chatId, List<User> users, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doAddUserToChat(chatId, users, tenantKey);
    }

    public UserInChatOperationResult deleteUserFromChat(String chatId, List<User> users) throws LarkClientException {
        checkAppType(false);
        return doDeleteUserFromChat(chatId, users, null);
    }

    public UserInChatOperationResult deleteUserFromChatIsv(String chatId, List<User> users, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return doDeleteUserFromChat(chatId, users, tenantKey);
    }

    public void disbandChat(String chatId) throws LarkClientException {
        checkAppType(false);
        doDisbandChat(chatId, null);
    }

    public void disbandChatIsv(String chatId, String tenantKey) throws LarkClientException {
        checkAppType(true);
        doDisbandChat(chatId, tenantKey);
    }

    public String genericPost(String path, String postData) throws LarkClientException {
        checkAppType(false);
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(null);
            return openApiClient.genericPost(path, tenantAccessToken, postData);
        }, null);
    }

    public String genericPostIsv(String path, String postData, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            return openApiClient.genericPost(path, tenantAccessToken, postData);
        }, tenantKey);
    }

    public String genericGet(String path) throws LarkClientException {
        checkAppType(false);
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(null);
            return openApiClient.genericGet(path, tenantAccessToken);
        }, null);
    }

    public String genericGetIsv(String path, String tenantKey) throws LarkClientException {
        checkAppType(true);
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            return openApiClient.genericGet(path, tenantAccessToken);
        }, tenantKey);
    }


    private GroupInfoResult doFetchGroupInfo(String chatId, String tenantKey) throws LarkClientException {

        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            FetchChatInfoRequest req = new FetchChatInfoRequest();
            req.setChatId(chatId);
            FetchChatInfoResponse resp = openApiClient.fetchChatInfo(tenantAccessToken, req);

            GroupInfoResult result = new GroupInfoResult();
            result.setAvatar(resp.getData().getAvatar());
            result.setChatId(resp.getData().getChatId());
            result.setDescription(resp.getData().getDescription());
            result.setI18nNames(resp.getData().getI18nNames());
            result.setMembers(resp.getData().getMembers());
            result.setName(resp.getData().getName());
            result.setOwnerOpenId(resp.getData().getOwnerOpenId());
            result.setOwnerUserId(resp.getData().getOwnerUserId());
            return result;
        }, tenantKey);
    }

    private GroupListResult doFetchGroupList(Integer pageSize, String pageToken, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);

            FetchChatListRequest req = new FetchChatListRequest();
            req.setPageSize(pageSize);
            req.setPageToken(pageToken);

            FetchChatListResponse resp = openApiClient.fetchChatList(tenantAccessToken, req);
            GroupListResult result = new GroupListResult();
            result.setGroups(resp.getData().getGroups());
            result.setHasMore(resp.getData().getHasMore());
            result.setPageToken(resp.getData().getPageToken());
            return result;
        }, tenantKey);
    }

    private UploadImageResult doUploadImage(String originName, InputStream imageFile, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {


            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);

            UploadImageRequest req = new UploadImageRequest();
            req.setOriginName(originName);
            req.setImageFile(imageFile);
            UploadImageResponse resp = openApiClient.uploadImage(tenantAccessToken, req);

            UploadImageResult result = new UploadImageResult();
            result.setImageKey(resp.getData().getImageKey());
            return result;
        }, tenantKey);
    }

    private InputStream doFetchImage(String imageKey, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            FetchImageRequest req = new FetchImageRequest();
            req.setImageKey(imageKey);
            return openApiClient.fetchImage(tenantAccessToken, req);

        }, tenantKey);
    }

    private UserInChatOperationResult doCreateChat(ChatInfo chatInfo, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);

            CreateChatRequest req = new CreateChatRequest();
            req.setUserIds(chatInfo.getUserIds());
            req.setOpenIds(chatInfo.getOpenIds());
            req.setName(chatInfo.getName());
            req.setOnlyOwnerAdd(chatInfo.getOnlyOwnerAdd());
            req.setShareAllowed(chatInfo.getShareAllowed());
            req.setOnlyOwnerAtAll(chatInfo.getOnlyOwnerAtAll());
            req.setOnlyOwnerEdit(chatInfo.getOnlyOwnerEdit());
            req.setI18nNames(chatInfo.getI18nNames());

            CreateChatResponse resp = openApiClient.createChat(tenantAccessToken, req);

            UserInChatOperationResult result = new UserInChatOperationResult();
            result.setChatId(resp.getData().getChatId());
            result.setInvalidOpenIds(resp.getData().getInvalidOpenIds());
            result.setInvalidUserIds(resp.getData().getInvalidUserIds());
            return result;
        }, tenantKey);
    }

    private String doUpdateChatInfo(String chatId, ChatInfo chatInfo, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            UpdateChatInfoRequest req = new UpdateChatInfoRequest();
            req.setChatId(chatId);
            req.setOwnerUserId(chatInfo.getOwnerUserId());
            req.setOwnerOpenId(chatInfo.getOwnerOpenId());
            req.setName(chatInfo.getName());
            req.setOnlyOwnerAdd(chatInfo.getOnlyOwnerAdd());
            req.setShareAllowed(chatInfo.getShareAllowed());
            req.setOnlyOwnerAtAll(chatInfo.getOnlyOwnerAtAll());
            req.setOnlyOwnerEdit(chatInfo.getOnlyOwnerEdit());
            req.setI18nNames(chatInfo.getI18nNames());

            UpdateChatInfoResponse resp = openApiClient.updateChatInfo(tenantAccessToken, req);
            return resp.getData().getChatId();
        }, tenantKey);
    }

    private UserInChatOperationResult doAddUserToChat(String chatId, List<User> users, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);

            AddUserToChatRequest req = new AddUserToChatRequest();
            req.setChatId(chatId);

            List<String> openIds = new ArrayList<>(users.size());
            List<String> userIds = new ArrayList<>(users.size());
            for (User user : users) {
                if (user.getOpenId() != null) {
                    openIds.add(user.getOpenId());
                } else if (user.getUserId() != null) {
                    userIds.add(user.getUserId());
                }
            }
            req.setOpenIds(openIds);
            req.setUserIds(userIds);

            AddUserToChatResponse resp = openApiClient.addUserToChat(tenantAccessToken, req);
            UserInChatOperationResult result = new UserInChatOperationResult();
            result.setChatId(chatId);
            result.setInvalidOpenIds(resp.getData().getInvalidOpenIds());
            result.setInvalidUserIds(resp.getData().getInvalidUserIds());
            return result;
        }, tenantKey);
    }

    private UserInChatOperationResult doDeleteUserFromChat(String chatId, List<User> users, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            DeleteUserFromChatRequest req = new DeleteUserFromChatRequest();
            req.setChatId(chatId);

            List<String> openIds = new ArrayList<>(users.size());
            List<String> userIds = new ArrayList<>(users.size());
            for (User user : users) {
                if (user.getOpenId() != null) {
                    openIds.add(user.getOpenId());
                } else if (user.getUserId() != null) {
                    userIds.add(user.getUserId());
                }
            }
            req.setOpenIds(openIds);
            req.setUserIds(userIds);

            DeleteUserFromChatResponse resp = openApiClient.deleteUserFromChat(tenantAccessToken, req);
            UserInChatOperationResult result = new UserInChatOperationResult();
            result.setChatId(chatId);
            result.setInvalidOpenIds(resp.getData().getInvalidOpenIds());
            result.setInvalidUserIds(resp.getData().getInvalidUserIds());
            return result;
        }, tenantKey);
    }

    private void doDisbandChat(String chatId, String tenantKey) throws LarkClientException {
        retryIfTenantAccessTokenInvalid(() -> {
            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);

            DisbandChatRequest req = new DisbandChatRequest();
            req.setChatId(chatId);
            openApiClient.disbandChat(tenantAccessToken, req);
            return null;
        }, tenantKey);
    }

    private BatchSendChatMessageResult doBatchSendChatMessage(BatchMessageDestination dest, Message message, String tenantKey) throws LarkClientException {
        return retryIfTenantAccessTokenInvalid(() -> {

            final String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);
            SendMessageBatchRequest req = new SendMessageBatchRequest();

            req.setMsgType(message.getMsgType());

            req.setDepartmentIds(dest.getDepartmentIds());
            req.setOpenIds(dest.getOpenIds());
            req.setUserIds(dest.getUserIds());

            if (message instanceof CardMessage) {
                req.setUpdateMulti(((CardMessage) message).getUpdateMulti());
                req.setCard(message.getContent());
            } else {
                req.setContent(MixUtils.newHashMap(message.getContentKey(), message.getContent()));
            }

            SendMessageBatchResponse resp = openApiClient.sendMessageBatch(tenantAccessToken, req);
            BatchSendChatMessageResult result = new BatchSendChatMessageResult();
            result.setMessageId(resp.getData().getMessageId());
            result.setInvalidDepartmentIds(resp.getData().getInvalidDepartmentIds());
            result.setInvalidOpenIds(resp.getData().getInvalidOpenIds());
            result.setInvalidUserIds(resp.getData().getInvalidUserIds());

            return result;
        }, tenantKey);

    }

    private String doSendChatMessage(MessageDestination dest, Message message, String tenantKey) throws LarkClientException {
        if (dest == null || message == null || MixUtils.isBlankString(dest.identity())) {
            throw new IllegalArgumentException("destination or message null");
        }

        return retryIfTenantAccessTokenInvalid(() -> {
            String tenantAccessToken = getTenantAccessTokenOrException(tenantKey);

            final String destIdentity = dest.identity();
            SendMessageRequest req = new SendMessageRequest();
            if (dest instanceof MessageDestinations.OpenId) {
                req.setOpenId(destIdentity);
            } else if (dest instanceof MessageDestinations.ChatId) {
                req.setChatId(destIdentity);
            } else if (dest instanceof MessageDestinations.UserId) {
                req.setUserId(destIdentity);
            } else if (dest instanceof MessageDestinations.Email) {
                req.setEmail(destIdentity);
            }

            req.setRootId(message.getRootId());
            req.setMsgType(message.getMsgType());

            if (message instanceof CardMessage) {
                req.setUpdateMulti(((CardMessage) message).getUpdateMulti());
                req.setCard(message.getContent());
            } else {
                req.setContent(MixUtils.newHashMap(message.getContentKey(), message.getContent()));
            }

            SendMessageResponse sendMessageResponse = openApiClient.sendMessage(tenantAccessToken, req);
            return sendMessageResponse.getData().getMessageId();
        }, tenantKey);

    }

    private String getTenantAccessTokenOrException(String tenantKey) throws LarkClientException {
        String tenantAccessToken = instanceContext.getTokenCenter().getTenantAccessToken(tenantKey);
        if (tenantAccessToken == null) {
            throw new IllegalStateException("get tenantAccessToken failed, appId: " + appId + ", tenantKey: " + tenantKey);
        }
        return tenantAccessToken;
    }

    private <T> T retryIfTenantAccessTokenInvalid(LarkClientCallable<T> c, String tenantKey) throws LarkClientException {
        try {
            return c.call();
        } catch (LarkClientException le) {
            if (le.getCode() == LarkClientException.TENANT_ACCESS_TOKEN_INVALID) {
                instanceContext.getTokenCenter().refreshTenantToken(tenantKey);
                return c.call();
            }
            throw le;
        }
    }

    private void checkAppType(boolean isIsvFunction) throws UnsupportedOperationException {
        if (isIsvFunction ^ instanceContext.getApp().getIsIsv()) {
            throw new UnsupportedOperationException("ISV app send message should use method sendChatMessageIsv");
        }
    }

    private interface LarkClientCallable<T> {
        T call() throws LarkClientException;
    }

    @Getter
    @Setter
    @ToString
    public static class BatchSendChatMessageResult {
        private List<String> invalidDepartmentIds;

        private List<String> invalidOpenIds;

        private List<String> invalidUserIds;

        private String messageId;
    }

    @Getter
    @Setter
    @ToString
    public static class UploadImageResult {
        private String imageKey;
    }

    @Getter
    @Setter
    @ToString
    public static class GroupListResult {
        private String pageToken;
        private Boolean hasMore;
        private List<Group> groups;
    }

    @Getter
    @Setter
    @ToString
    public static class GroupInfoResult {
        private String avatar;

        private String chatId;

        private String description;

        private I18nText i18nNames;

        private List<User> members;

        private String name;

        private String ownerOpenId;

        private String ownerUserId;
    }

    @Getter
    @Setter
    @ToString
    public static class UserInChatOperationResult {
        private String chatId;
        private List<String> invalidOpenIds;
        private List<String> invalidUserIds;
    }
}
