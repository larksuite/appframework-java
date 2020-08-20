package com.larksuite.appframework.sdk.core.protocol.client.chat;

import com.larksuite.appframework.sdk.core.protocol.*;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/20
 */
public class ChatClient extends OpenApiClient.AbstractSubClient {
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


    public FetchChatInfoResponse fetchChatInfo(String tenantAccessToken, FetchChatInfoRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + FetchChatInfoPath, req), DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken)), FetchChatInfoResponse.class);
    }

    public FetchChatListResponse fetchChatList(String tenantAccessToken, FetchChatListRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + FetchChatListPath, req), DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken)), FetchChatListResponse.class);
    }

    public UpdateChatInfoResponse updateChatInfo(String tenantAccessToken, UpdateChatInfoRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + UpdateChatInfoPath, DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), UpdateChatInfoResponse.class);
    }

    public CreateChatResponse createChat(String tenantAccessToken, CreateChatRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + CreateChatPath, DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), CreateChatResponse.class);
    }

    public AddUserToChatResponse addUserToChat(String tenantAccessToken, AddUserToChatRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + AddUserToChatPath, DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), AddUserToChatResponse.class);
    }

    public DeleteUserFromChatResponse deleteUserFromChat(String tenantAccessToken, DeleteUserFromChatRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + DeleteUserFromChatPath, DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), DeleteUserFromChatResponse.class);
    }

    public DisbandChatResponse disbandChat(String tenantAccessToken, DisbandChatRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + DisbandChatPath, DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(req)), DisbandChatResponse.class);
    }


    public MiniProgramLoginResponse miniProgramLoginValidate(String appAccessToken, MiniProgramLoginRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + MiniProgramLoginValidatePath, DEFAULT_CONN_TIMEOUT, 3000, createHeaderWithAuthorization(appAccessToken), JsonUtil.larkFormatToJsonString(req)), MiniProgramLoginResponse.class);
    }


    protected ChatClient(OpenApiClient parent) {
        super(parent);
    }
}
