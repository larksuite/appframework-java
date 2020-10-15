package com.larksuite.appframework.sdk.core.protocol.client.contact;

import com.larksuite.appframework.sdk.core.protocol.client.BaseClientTest;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/24
 */
@Slf4j
public class ContactClientTest extends BaseClientTest {
    static ContactClient contactClient;

    private String departmentId = System.getenv("department_id");

    @BeforeAll
    public static void setup() {
        contactClient = openApiClient.contactClient();
    }

//    @Test
//    public void testScope() throws LarkClientException {
//        ScopeResponse scope = contactClient.scope(tenantAccessToken);
//        Assertions.assertNotNull(scope);
//        log.info("{}", scope);
//    }
//
//    @Test
//    public void testInfo() throws LarkClientException {
//        InfoRequest request = new InfoRequest();
//        request.setDepartmentId(departmentId);
//        InfoResponse response = contactClient.info(tenantAccessToken, request);
//        log.info("{}", response);
//        Assertions.assertNotNull(response);
//    }
//
//    @Test
//    public void testSimple() throws LarkClientException {
//        SimpleRequest request = new SimpleRequest();
//        request.setDepartmentId(departmentId);
//        SimpleResponse response = contactClient.simple(tenantAccessToken, request);
//        log.info("{}", response);
//        Assertions.assertNotNull(response);
//    }
//
//    @Test
//    public void testUserList() throws LarkClientException {
//        UserListRequest request = new UserListRequest();
//        request.setDepartmentId(departmentId);
//        UserListResponse response = contactClient.userList(tenantAccessToken, request);
//        log.info("{}", response);
//        Assertions.assertNotNull(response);
//    }
//
//    @Test
//    public void testUserDetail() throws LarkClientException {
//        UserDetailRequest request = new UserDetailRequest();
//        request.setDepartmentId(departmentId);
//        UserDetailResponse response = contactClient.userDetail(tenantAccessToken, request);
//        log.info("{}", response);
//        Assertions.assertNotNull(response);
//    }
//
//    @Test
//    public void testUserBatch() throws LarkClientException {
//        BatchUserRequest request = new BatchUserRequest();
//        request.setOpenIds(new LinkedList<>());
//        request.getOpenIds().add(System.getenv("open_id_user1"));
//        request.getOpenIds().add(System.getenv("open_id_user2"));
//        UserDetailResponse response = contactClient.userBatch(tenantAccessToken, request);
//        log.info("{}", response);
//        Assertions.assertNotNull(response);
//    }
}