package com.larksuite.appframework.sdk.core.protocol.client;

import com.larksuite.appframework.sdk.core.protocol.OpenApiClient;
import com.larksuite.appframework.sdk.utils.SimpleHttpClient;

/**
 * @author xiaobozhang@aliyun.com
 * @since 2020/8/19
 */
public class BaseClientTest {
    public static OpenApiClient openApiClient = new OpenApiClient(new SimpleHttpClient(), System.getenv("base_path"));

    public static String tenantAccessToken = System.getenv("tenant_access_token");
    public static String userAccessToken = System.getenv("user_access_token");

}
