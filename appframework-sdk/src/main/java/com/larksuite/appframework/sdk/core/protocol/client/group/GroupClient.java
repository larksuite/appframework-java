package com.larksuite.appframework.sdk.core.protocol.client.group;

import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.exception.LarkClientException;

import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_CONN_TIMEOUT;
import static com.larksuite.appframework.sdk.core.protocol.OpenApiClient.DEFAULT_READ_TIMEOUT;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/10
 */
public class GroupClient extends OpenApiClient.AbstractSubClient {

    /**
     * group
     */
    private static final String GROUP_LIST = "/open-apis/user/v4/group_list";
    /**
     * members
     */
    private static final String MEMBERS = "/open-apis/chat/v4/members";

    /**
     * SEARCH
     */
    private static final String SEARCH = "/open-apis/chat/v4/search";

    protected GroupClient(OpenApiClient parent) {
        super(parent);
    }

    /**
     * 获取用户所在的群列表
     * @param userAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public GroupListResponse groupList(String userAccessToken, GroupListRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + GROUP_LIST, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(userAccessToken))
                , GroupListResponse.class);
    }

    /**
     * 获取群成员列表
     * @param userAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public MembersResponse members(String userAccessToken, MembersRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + MEMBERS, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT,
                createHeaderWithAuthorization(userAccessToken)), MembersResponse.class);
    }

    /**
     * 搜索用户所在的群列表
     * @param userAccessToken
     * @param request
     * @return
     * @throws LarkClientException
     */
    public SearchResponse search(String userAccessToken, SearchRequest request) throws LarkClientException {
        return call(() -> getHttpClient().doGet(buildGetUrl(getBasePath() + SEARCH, request), DEFAULT_CONN_TIMEOUT, DEFAULT_READ_TIMEOUT, createHeaderWithAuthorization(userAccessToken)), SearchResponse.class);
    }
}
