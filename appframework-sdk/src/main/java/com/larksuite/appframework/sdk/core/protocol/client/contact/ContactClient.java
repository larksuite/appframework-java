package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;
import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_READ_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/17
 */
public class ContactClient extends OpenApiClient.AbstractSubClient {

    private static final String SCOPE = "/open-apis/contact/v1/scope/get";
    private static final String INFO = "/open-apis/contact/v1/department/info/get";
    private static final String SIMPLE = "/open-apis/contact/v1/department/simple/list";
    private static final String USER = "/open-apis/contact/v1/department/user/list";
    private static final String DETAIL = "/open-apis/contact/v1/department/user/detail/list";
    private static final String USER_BATCH = "/open-apis/contact/v1/user/batch_get";

    /**
     * 获取通讯录授权范围
     *
     * @param tenantAccessToken
     * @return
     * @throws LarkClientException
     */
    public ScopeResponse scope(String tenantAccessToken) throws LarkClientException {
        return call(() -> getHttpClient().doGet(getBasePath() + SCOPE, DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), ScopeResponse.class);
    }

    /**
     * 获取部门详情
     *
     * @param tenantAccessToken
     * @param infoRequest
     * @return
     * @throws LarkClientException
     */
    public InfoResponse info(String tenantAccessToken, InfoRequest infoRequest) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + INFO, infoRequest), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), InfoResponse.class);
    }

    /**
     * 获取子部门列表
     *
     * @param tenantAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public SimpleResponse simple(String tenantAccessToken, SimpleRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + SIMPLE, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), SimpleResponse.class);
    }

    /**
     * 获取部门用户列表
     *
     * @param tenantAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public UserListResponse userList(String tenantAccessToken, UserListRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + USER, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), UserListResponse.class);
    }

    /**
     * 获取部门用户详情
     *
     * @param tenantAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public UserDetailResponse userDetail(String tenantAccessToken, UserDetailRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + DETAIL, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), UserDetailResponse.class);
    }

    /**
     * 批量获取用户信息
     * @param tenantAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public UserDetailResponse userBatch(String tenantAccessToken, BatchUserRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + USER_BATCH, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(tenantAccessToken)), UserDetailResponse.class);
    }

    protected ContactClient(OpenApiClient parent) {
        super(parent);
    }
}
