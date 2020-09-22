/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.Version;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.HttpClient;
import com.larksuite.appframework.sdk.utils.HttpException;
import com.larksuite.appframework.sdk.utils.JsonUtil;
import com.larksuite.appframework.sdk.utils.MixUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenApiClient {

    private static final Map<String, String> BASE_HEADER = MixUtils.newHashMap("user-agent", String.format("appframework-java(%s)", Version.CURRENT));

    /**
     * auth
     */
    private static final String FetchAppAccessTokenInternalPath = "/open-apis/auth/v3/app_access_token/internal/";
    private static final String FetchAppAccessTokenIsvPath = "/open-apis/auth/v3/app_access_token/";
    private static final String FetchTenantAccessTokenInternalPath = "/open-apis/auth/v3/tenant_access_token/internal/";
    private static final String FetchTenantAccessTokenIsvPath = "/open-apis/auth/v3/tenant_access_token/";
    private static final String ResendAppTicketPath = "/open-apis/auth/v3/app_ticket/resend";

    /**
     * message
     */
    private static final String SendMessagePath = "/open-apis/message/v4/send/";
    private static final String SendMessageBatchPath = "/open-apis/message/v4/batch_send/";
    private static final String UploadImagePath = "/open-apis/image/v4/put/";
    private static final String FetchImagePath = "/open-apis/image/v4/get";
    /**
     * bot info
     */
    private static final String FetchBOTInfoPath = "/open-apis/bot/v3/info/";
    /**
     * chat
     */
    private static final String FetchChatInfoPath = "/open-apis/chat/v4/info/";
    private static final String FetchChatListPath = "/open-apis/chat/v4/list/";
    private static final String UpdateChatInfoPath = "/open-apis/chat/v4/update/";
    private static final String CreateChatPath = "/open-apis/chat/v4/create/";
    private static final String AddUserToChatPath = "/open-apis/chat/v4/chatter/add/";
    private static final String DeleteUserFromChatPath = "/open-apis/chat/v4/chatter/delete/";
    private static final String DisbandChatPath = "/open-apis/chat/v4/disband/";

    private static final String MiniProgramLoginValidatePath = "/open-apis/mina/v2/tokenLoginValidate";

    private static final String FEISHU_BASE_PATH = "https://open.feishu.cn";
    private static final String LARK_BASE_PATH = "https://open.larksuite.com";


    private HttpClient httpClient;

    private String basePath;

    public OpenApiClient(HttpClient httpClient, String basePath) {
        this.httpClient = httpClient;
        this.basePath = basePath;
    }

    public OpenApiClient(HttpClient httpClient, boolean isFeishu) {
        this(httpClient, isFeishu ? FEISHU_BASE_PATH : LARK_BASE_PATH);
    }

    public SendMessageResponse sendMessage(String tenantAccessToken, SendMessageRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + SendMessagePath, 3000, 5000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), SendMessageResponse.class);
    }

    public SendMessageBatchResponse sendMessageBatch(String tenantAccessToken, SendMessageBatchRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + SendMessageBatchPath, 3000, 5000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), SendMessageBatchResponse.class);
    }

    public UploadImageResponse uploadImage(String tenantAccessToken, UploadImageRequest req) throws LarkClientException {
        return call(() -> {
            List<HttpClient.Field> fl = new ArrayList<>(2);
            fl.add(new HttpClient.FileField("image", req.getImageFile(), req.getOriginName()));
            fl.add(new HttpClient.DataField("image_type", "message"));

            return httpClient.doPostFile(basePath + UploadImagePath,
                    3000, 5000,
                    createHeaderWithAuthorization(tenantAccessToken), fl);

        }, UploadImageResponse.class);
    }

    public FetchBotInfoResponse fetchBotInfo(String tenantAccessToken) throws LarkClientException {
        return call(() -> httpClient.doGet(basePath + FetchBOTInfoPath, 3000, 3000, createHeaderWithAuthorization(tenantAccessToken)), FetchBotInfoResponse.class);
    }

    public FetchChatInfoResponse fetchChatInfo(String tenantAccessToken, FetchChatInfoRequest req) throws LarkClientException {
        return call(() -> httpClient.doGet(buildGetUrl(basePath + FetchChatInfoPath, req), 3000, 3000, createHeaderWithAuthorization(tenantAccessToken)), FetchChatInfoResponse.class);
    }

    public FetchChatListResponse fetchChatList(String tenantAccessToken, FetchChatListRequest req) throws LarkClientException {
        return call(() -> httpClient.doGet(buildGetUrl(basePath + FetchChatListPath, req), 3000, 3000, createHeaderWithAuthorization(tenantAccessToken)), FetchChatListResponse.class);
    }

    public UpdateChatInfoResponse updateChatInfo(String tenantAccessToken, UpdateChatInfoRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + UpdateChatInfoPath, 3000, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), UpdateChatInfoResponse.class);
    }

    public CreateChatResponse createChat(String tenantAccessToken, CreateChatRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + CreateChatPath, 3000, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), CreateChatResponse.class);
    }

    public AddUserToChatResponse addUserToChat(String tenantAccessToken, AddUserToChatRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + AddUserToChatPath, 3000, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), AddUserToChatResponse.class);
    }

    public DeleteUserFromChatResponse deleteUserFromChat(String tenantAccessToken, DeleteUserFromChatRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + DeleteUserFromChatPath, 3000, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), DeleteUserFromChatResponse.class);
    }

    public DisbandChatResponse disbandChat(String tenantAccessToken, DisbandChatRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + DisbandChatPath, 3000, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), DisbandChatResponse.class);
    }

    public FetchAppAccessTokenInternalResponse fetchAppAccessTokenInternal(FetchAppAccessTokenInternalRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + FetchAppAccessTokenInternalPath, 3000, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchAppAccessTokenInternalResponse.class);
    }

    public FetchAppAccessTokenIsvResponse fetchAppAccessTokenIsv(FetchAppAccessTokenIsvRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + FetchAppAccessTokenIsvPath, 3000, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchAppAccessTokenIsvResponse.class);
    }

    public FetchTenantAccessTokenInternalResponse fetchTenantAccessTokenInternal(FetchTenantAccessTokenInternalRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + FetchTenantAccessTokenInternalPath, 3000, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchTenantAccessTokenInternalResponse.class);
    }

    public FetchTenantAccessTokenIsvResponse fetchTenantAccessTokenIsv(FetchTenantAccessTokenIsvRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + FetchTenantAccessTokenIsvPath, 3000, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchTenantAccessTokenIsvResponse.class);
    }

    public ResendAppTicketResponse resendAppTicket(ResendAppTicketRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + ResendAppTicketPath, 3000, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), ResendAppTicketResponse.class);
    }

    public MiniProgramLoginResponse miniProgramLoginValidate(String appAccessToken, MiniProgramLoginRequest req) throws LarkClientException {
        return call(() -> httpClient.doPostJson(basePath + MiniProgramLoginValidatePath, 3000, 3000, createHeaderWithAuthorization(appAccessToken), JsonUtil.larkFormatToJsonString(req)), MiniProgramLoginResponse.class);
    }

    public InputStream fetchImage(String tenantAccessToken, FetchImageRequest req) throws LarkClientException {
        try {
            return httpClient.doGetFile(buildGetUrl(basePath + FetchImagePath, req), 3000, 3000, createHeaderWithAuthorization(tenantAccessToken));
        } catch (HttpException e) {
            throw translateToLarkClientException(e);
        }
    }

    public String genericPost(String path, String accessToken, Object req) throws LarkClientException {
        try {
            return httpClient.doPostJson(basePath + path, 3000, 5000, createHeaderWithAuthorization(accessToken), JsonUtil.larkFormatToJsonString(req));
        } catch (HttpException e) {
            throw translateToLarkClientException(e);
        }
    }

    public String genericGet(String path, String accessToken) throws LarkClientException {
        try {
            return httpClient.doGet(basePath + path, 3000, 5000, createHeaderWithAuthorization(accessToken));
        } catch (HttpException e) {
            throw translateToLarkClientException(e);
        }
    }

    private static <T extends BaseResponse> T transCodeToException(T t) throws LarkClientException {
        if (t.getCode() == 0) {
            return t;
        }
        throw new LarkClientException(t.getCode(), t.getMsg());
    }

    private static Map<String, String> newHeader() {
        return new HashMap<>(BASE_HEADER);
    }

    private static Map<String, String> createHeaderWithAuthorization(String token) {
        Map<String, String> header = newHeader();
        header.put("Authorization", "Bearer " + token);
        return header;
    }

    private static <T extends BaseResponse> T call(RemoteCaller c, Class<T> responseClass) throws LarkClientException {
        try {
            String s = c.call();

            T resp = JsonUtil.larkFormatToJavaObject(s, responseClass);

            transCodeToException(resp);
            return resp;
        } catch (HttpException e) {
            throw translateToLarkClientException(e);
        } catch (IOException e) {
            throw new LarkClientException(LarkClientException.RESPONSE_DATA_PARSE_ERR_CODE, e.getMessage());
        }
    }

    private static LarkClientException translateToLarkClientException(HttpException e) {

        if (e.getHttpCode() == 400) {
            try {
                BaseResponse re = JsonUtil.toJavaObject(e.getResponse(), BaseResponse.class);
                if (re.getCode() == LarkClientException.TENANT_ACCESS_TOKEN_INVALID) {
                    return new LarkClientException(LarkClientException.TENANT_ACCESS_TOKEN_INVALID, e.getMessage());
                }
            } catch (IOException ioe) {
                // if response is not a json string, just ignore
            }
        }
        return new LarkClientException(LarkClientException.REQUEST_FAIL_ERR_CODE, e.getMessage());
    }

    private interface RemoteCaller {
        String call() throws HttpException, IOException;
    }

    private String buildGetUrl(String url, Object o) {
        Map m;
        if (o instanceof Map) {
            m = (Map) o;
        } else {
            m = JsonUtil.larkFormatConvertToJavaObject(o, Map.class);
        }

        StringBuilder sb = new StringBuilder(128);
        sb.append(url).append("?");

        m.forEach((k, v) -> sb.append(k.toString()).append("=").append(v.toString()).append("&"));

        if (sb.charAt(sb.length() - 1) == '&') {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
