package com.larksuite.appframework.sdk.core.protocol.client.auth;

import com.larksuite.appframework.sdk.core.protocol.*;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import com.larksuite.appframework.sdk.utils.JsonUtil;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/20
 */
public class AuthClient extends OpenApiClient.AbstractSubClient {
    /**
     * auth
     */
    private static final String FetchAppAccessTokenInternalPath = "/open-apis/auth/v3/app_access_token/internal/";
    private static final String FetchAppAccessTokenIsvPath = "/open-apis/auth/v3/app_access_token/";
    private static final String FetchTenantAccessTokenInternalPath = "/open-apis/auth/v3/tenant_access_token/internal/";
    private static final String FetchTenantAccessTokenIsvPath = "/open-apis/auth/v3/tenant_access_token/";
    private static final String ResendAppTicketPath = "/open-apis/auth/v3/app_ticket/resend";


    public FetchAppAccessTokenInternalResponse fetchAppAccessTokenInternal(FetchAppAccessTokenInternalRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + FetchAppAccessTokenInternalPath, DEFAULT_CONN_TIMEOUT, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchAppAccessTokenInternalResponse.class);
    }

    public FetchAppAccessTokenIsvResponse fetchAppAccessTokenIsv(FetchAppAccessTokenIsvRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + FetchAppAccessTokenIsvPath, DEFAULT_CONN_TIMEOUT, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchAppAccessTokenIsvResponse.class);
    }

    public FetchTenantAccessTokenInternalResponse fetchTenantAccessTokenInternal(FetchTenantAccessTokenInternalRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + FetchTenantAccessTokenInternalPath, DEFAULT_CONN_TIMEOUT, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchTenantAccessTokenInternalResponse.class);
    }

    public FetchTenantAccessTokenIsvResponse fetchTenantAccessTokenIsv(FetchTenantAccessTokenIsvRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + FetchTenantAccessTokenIsvPath, DEFAULT_CONN_TIMEOUT, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), FetchTenantAccessTokenIsvResponse.class);
    }

    public ResendAppTicketResponse resendAppTicket(ResendAppTicketRequest req) throws LarkClientException {
        return call(() -> getHttpClient().doPostJson(getBasePath() + ResendAppTicketPath, DEFAULT_CONN_TIMEOUT, 3000, newHeader(), JsonUtil.larkFormatToJsonString(req)), ResendAppTicketResponse.class);
    }

    protected AuthClient(OpenApiClient parent) {
        super(parent);
    }

}
