package com.larksuite.appframework.sdk.core.protocol.client.bot;

import com.larksuite.appframework.sdk.core.protocol.BaseResponse;
import com.larksuite.appframework.sdk.core.protocol.client.BaseClientTest;
import com.larksuite.appframework.sdk.exception.LarkClientException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/19
 */
@Slf4j
public class BotClientTest extends BaseClientTest {

//    @Test
//    public void testInfo() throws LarkClientException {
//        BotInfoResponse info = openApiClient.botClient().info(tenantAccessToken);
//        Assertions.assertNotNull(info);
//        log.info("{}", info);
//    }
//
//    @Test
//    public void testRemove() throws LarkClientException {
//        ChatIdRequest request = new ChatIdRequest();
//        request.setChatId("oc_f4b9eb54278968cf23de50dd4da463ce");
//        BaseResponse response = openApiClient.botClient().remove(tenantAccessToken, request);
//        Assertions.assertNotNull(response);
//        log.info("{}", response);
//    }
//
//    @Test
//    public void testAdd() throws LarkClientException {
//        ChatIdRequest request = new ChatIdRequest();
//        request.setChatId("oc_f4b9eb54278968cf23de50dd4da463ce");
//        BaseResponse response = openApiClient.botClient().add(tenantAccessToken, request);
//        Assertions.assertNotNull(response);
//        log.info("{}", response);
//    }
}