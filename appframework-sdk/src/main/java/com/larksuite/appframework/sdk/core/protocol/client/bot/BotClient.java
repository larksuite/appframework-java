package com.larksuite.appframework.sdk.core.protocol.client.bot;

import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;
import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_READ_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/11
 */
public class BotClient extends OpenApiClient.AbstractSubClient {
    private static final String INFO = "/open-apis/bot/v3/info/";
    private static final String ADD = "/open-apis/bot/v4/add";
    private static final String REMOVE = "/open-apis/bot/v4/remove";


    /**
     * 获取机器人信息
     *
     * @param tenantAccessToken
     * @return
     * @throws LarkClientException
     */
    public BotInfoResponse info(String tenantAccessToken) throws LarkClientException {
        return call(() -> getHttpClient().doGet(getBasePath() + INFO, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), BotInfoResponse.class);
    }

    /**
     * 拉机器人进群
     *
     * @param tenantAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public BaseResponse add(String tenantAccessToken, ChatIdRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + ADD, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)), BaseResponse.class);
    }

    /**
     * 将机器人移出群
     *
     * @param tenantAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public BaseResponse remove(String tenantAccessToken, ChatIdRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + REMOVE, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken), JsonUtil.larkFormatToJsonString(request)), BaseResponse.class);
    }

    protected BotClient(OpenApiClient parent) {
        super(parent);
    }
}
