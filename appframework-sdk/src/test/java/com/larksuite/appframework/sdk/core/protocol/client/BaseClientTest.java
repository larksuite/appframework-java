package com.larksuite.appframework.sdk.core.protocol.client;

import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.utils.SimpleHttpClient;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/19
 */
public class BaseClientTest {
    public static OpenApiClient openApiClient = new OpenApiClient(new SimpleHttpClient(), "https://privateZone");

    public static String tenantAccessToken = "t-84a3451769eb362d1c81813fef0a5daee59e4b62";
    public static String userAccessToken = "u-3VNrSFD0YvoiHZN3yyZyDe";

}
