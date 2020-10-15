package com.larksuite.appframework.sdk.core.protocol.client.group;

import com.larksuite.appframework.sdk.core.protocol.client.BaseClientTest;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/24
 */
@Slf4j
public class GroupClientTest extends BaseClientTest {

    private static GroupClient groupClient;

    @BeforeClass
    public static void setup() {
        groupClient = openApiClient.groupClient();
    }

    @Test
    public void testGroupList() throws LarkClientException {
        GroupListResponse response = groupClient.groupList(userAccessToken, new GroupListRequest());
        log.info("{}", response);
        Assert.assertNotNull(response);
    }

    @Test
    public void testMembers() throws LarkClientException {
        MembersRequest request = new MembersRequest();
        request.setChatId(System.getenv("chat_id"));
        MembersResponse response = groupClient.members(userAccessToken, request);
        log.info("{}", response);
        Assert.assertNotNull(response);
    }

    @Test
    public void testSearch() throws LarkClientException {
        SearchRequest request = new SearchRequest();
        request.setQuery("test");
        SearchResponse response = groupClient.search(userAccessToken, request);
        log.info("{}", response);
        Assert.assertNotNull(response);
    }
}