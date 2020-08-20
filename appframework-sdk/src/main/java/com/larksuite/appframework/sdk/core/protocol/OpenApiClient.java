/*
 * Copyright (c) 2019 Bytedance Inc.  All rights reserved.
 * Use of this source code is governed by a MIT style
 * license that can be found in the LICENSE file.
 */

package com.larksuite.appframework.sdk.core.protocol;

import com.larksuite.appframework.sdk.Version;
import com.larksuite.appframework.sdk.core.protocol.client.auth.AuthClient;
import com.larksuite.appframework.sdk.core.protocol.client.bot.BotClient;
import com.larksuite.appframework.sdk.core.protocol.client.calendar.CalendarClient;
import com.larksuite.appframework.sdk.core.protocol.client.chat.ChatClient;
import com.larksuite.appframework.sdk.core.protocol.client.contact.ContactClient;
import com.larksuite.appframework.sdk.core.protocol.client.group.GroupClient;
import com.larksuite.appframework.sdk.core.protocol.client.message.MessageClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.exception.LarkClientInitException;
import com.larksuite.appframework.sdk.utils.HttpClient;
import com.larksuite.appframework.sdk.utils.HttpException;
import com.larksuite.appframework.sdk.utils.JsonUtil;
import com.larksuite.appframework.sdk.utils.MixUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
public class OpenApiClient {

    private static final Map<String, String> BASE_HEADER = MixUtils.newHashMap("user-agent", String.format("appframework-java(%s)", Version.CURRENT));

    public static final int DEFAULT_CONN_TIMEOUT = 3000;
    public static final int DEFAULT_READ_TIMEOUT = 5000;


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

    public GroupClient groupClient() {
        return getInstance(GroupClient.class);
    }

    public BotClient botClient() {
        return getInstance(BotClient.class);
    }

    public CalendarClient calendarClient() {
        return getInstance(CalendarClient.class);
    }

    public ContactClient contactClient() {
        return getInstance(ContactClient.class);
    }

    public ChatClient chatClient() {
        return getInstance(ChatClient.class);
    }

    public MessageClient messageClient() {
        return getInstance(MessageClient.class);
    }

    public AuthClient authClient() {
        return getInstance(AuthClient.class);
    }

    public String genericPost(String path, String accessToken, Object req) throws LarkClientException {
        try {
            return httpClient.doPostJson(basePath + path, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, AbstractSubClient.createHeaderWithAuthorization(accessToken), JsonUtil.larkFormatToJsonString(req));
        } catch (HttpException e) {
            throw AbstractSubClient.translateToLarkClientException(e);
        }
    }

    public String genericGet(String path, String accessToken) throws LarkClientException {
        try {
            return httpClient.doGet(basePath + path, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, AbstractSubClient.createHeaderWithAuthorization(accessToken));
        } catch (HttpException e) {
            throw AbstractSubClient.translateToLarkClientException(e);
        }
    }

    /**
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.authClient()
     */
    @Deprecated
    public FetchAppAccessTokenInternalResponse fetchAppAccessTokenInternal(FetchAppAccessTokenInternalRequest req) throws LarkClientException {
        return authClient().fetchAppAccessTokenInternal(req);
    }

    /**
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.authClient()
     */
    @Deprecated
    public FetchAppAccessTokenIsvResponse fetchAppAccessTokenIsv(FetchAppAccessTokenIsvRequest req) throws LarkClientException {
        return authClient().fetchAppAccessTokenIsv(req);
    }

    /**
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.authClient()
     */
    @Deprecated
    public FetchTenantAccessTokenInternalResponse fetchTenantAccessTokenInternal(FetchTenantAccessTokenInternalRequest req) throws LarkClientException {
        return authClient().fetchTenantAccessTokenInternal(req);
    }

    /**
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.authClient()
     */
    @Deprecated
    public FetchTenantAccessTokenIsvResponse fetchTenantAccessTokenIsv(FetchTenantAccessTokenIsvRequest req) throws LarkClientException {
        return authClient().fetchTenantAccessTokenIsv(req);
    }

    /**
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.authClient()
     */
    @Deprecated
    public ResendAppTicketResponse resendAppTicket(ResendAppTicketRequest req) throws LarkClientException {
        return authClient().resendAppTicket(req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.messageClient()
     */
    @Deprecated
    public SendMessageResponse sendMessage(String tenantAccessToken, SendMessageRequest req) throws LarkClientException {
        return messageClient().sendMessage(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.messageClient()
     */
    @Deprecated
    public SendMessageBatchResponse sendMessageBatch(String tenantAccessToken, SendMessageBatchRequest req) throws LarkClientException {
        return messageClient().sendMessageBatch(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.messageClient()
     */
    @Deprecated
    public UploadImageResponse uploadImage(String tenantAccessToken, UploadImageRequest req) throws LarkClientException {
        return messageClient().uploadImage(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.messageClient()
     */
    @Deprecated
    public InputStream fetchImage(String tenantAccessToken, FetchImageRequest req) throws LarkClientException {
        return messageClient().fetchImage(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public FetchChatInfoResponse fetchChatInfo(String tenantAccessToken, FetchChatInfoRequest req) throws LarkClientException {
        return chatClient().fetchChatInfo(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public FetchChatListResponse fetchChatList(String tenantAccessToken, FetchChatListRequest req) throws LarkClientException {
        return chatClient().fetchChatList(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public UpdateChatInfoResponse updateChatInfo(String tenantAccessToken, UpdateChatInfoRequest req) throws LarkClientException {
        return chatClient().updateChatInfo(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public CreateChatResponse createChat(String tenantAccessToken, CreateChatRequest req) throws LarkClientException {
        return chatClient().createChat(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public AddUserToChatResponse addUserToChat(String tenantAccessToken, AddUserToChatRequest req) throws LarkClientException {
        return chatClient().addUserToChat(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public DeleteUserFromChatResponse deleteUserFromChat(String tenantAccessToken, DeleteUserFromChatRequest req) throws LarkClientException {
        return chatClient().deleteUserFromChat(tenantAccessToken, req);
    }

    /**
     * @param tenantAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public DisbandChatResponse disbandChat(String tenantAccessToken, DisbandChatRequest req) throws LarkClientException {
        return chatClient().disbandChat(tenantAccessToken, req);
    }

    /**
     * @param appAccessToken
     * @param req
     * @return
     * @throws LarkClientException
     * @deprecated use openApiClient.chatClient()
     */
    @Deprecated
    public MiniProgramLoginResponse miniProgramLoginValidate(String appAccessToken, MiniProgramLoginRequest req) throws LarkClientException {
        return chatClient().miniProgramLoginValidate(appAccessToken, req);
    }

    /**
     * subClient
     */
    public abstract static class AbstractSubClient {

        private final OpenApiClient parent;


        protected interface RemoteCaller {
            String call() throws HttpException, IOException;
        }

        protected AbstractSubClient(OpenApiClient parent) {
            this.parent = parent;
        }

        protected HttpClient getHttpClient() {
            return parent.httpClient;
        }

        protected String getBasePath() {
            return parent.basePath;
        }

        protected static <T extends BaseResponse> T transCodeToException(T t) throws LarkClientException {
            if (t.getCode() == 0) {
                return t;
            }
            throw new LarkClientException(t.getCode(), t.getMsg());
        }

        protected static Map<String, String> newHeader() {
            return new HashMap<>(BASE_HEADER);
        }

        protected static Map<String, String> createHeaderWithAuthorization(String token) {
            Map<String, String> header = newHeader();
            header.put("Authorization", "Bearer " + token);
            return header;
        }

        protected static LarkClientException translateToLarkClientException(HttpException e) {
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

        protected static <T extends BaseResponse> T call(RemoteCaller c, Class<T> responseClass) throws LarkClientException {
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

        protected String buildGetUrl(String url, Object o) {
            Map m;
            if (o instanceof Map) {
                m = (Map) o;
            } else {
                m = JsonUtil.larkFormatConvertToJavaObject(o, Map.class);
            }

            StringBuilder sb = new StringBuilder(128);
            sb.append(url);
            if (!m.isEmpty()) {
                sb.append("?");
            }

            m.forEach((k, v) -> {
                if (v instanceof List) {
                    List.class.cast(v).forEach(value -> {
                        sb.append(k.toString()).append("=").append(value.toString()).append("&");
                    });
                } else {
                    sb.append(k.toString()).append("=").append(v.toString()).append("&");
                }
            });

            if (sb.charAt(sb.length() - 1) == '&') {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        }
    }


    private final ConcurrentMap<String, AbstractSubClient> subClients = new ConcurrentHashMap<>();

    private <T extends AbstractSubClient> T getInstance(Class<T> tClass) {
        return (T) subClients.computeIfAbsent(tClass.getName(), key -> {
            try {
                Constructor<T> constructor = tClass.getDeclaredConstructor(OpenApiClient.class);
                constructor.setAccessible(true);
                return constructor.newInstance(this);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                throw new LarkClientInitException(e.getMessage());
            }
        });
    }
}
